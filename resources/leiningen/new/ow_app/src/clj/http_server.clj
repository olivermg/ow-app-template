(ns {{name}}.http-server
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [org.httpkit.server :as https]))

(defmethod ig/init-key :{{name}}/http-server [_ {:keys [handlers] :as opts}]
  (info "starting {{name}}/http-server")
  (trace opts)
  (let [api-handler (:api handlers)
        web-handler (:web handlers)
        handler (fn [req]
                  (or (and api-handler (api-handler req))
                      (and web-handler (web-handler req))))]
    (-> opts
        (assoc-in [:handlers :main] handler)
        (assoc :server (https/run-server handler opts)))))

(defmethod ig/halt-key! :{{name}}/http-server [_ {:keys [server stop-timeout] :as opts}]
  (info "stopping {{name}}/http-server" opts)
  (server :timeout stop-timeout))
