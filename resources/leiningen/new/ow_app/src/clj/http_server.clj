(ns {{name}}.http-server
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [org.httpkit.server :as https]))

(defmethod ig/init-key :{{name}}/http-server [_ {:keys [handlers] :as opts}]
  (info "starting {{name}}/http-server")
  (trace opts)
  (let [handler (fn [req]
                  (or ((:api handlers) req)
                      ((:web handlers) req)))]
    (assoc opts
           :server (https/run-server handler opts))))

(defmethod ig/halt-key! :{{name}}/http-server [_ {:keys [server stop-timeout] :as opts}]
  (info "stopping {{name}}/http-server" opts)
  (server :timeout stop-timeout))
