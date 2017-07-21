(ns {{name}}.http-server
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [org.httpkit.server :as https]))

(defmethod ig/init-key :{{name}}/http-server [_ {:keys [handlers] :as opts}]
  (info "starting {{name}}/http-server")
  (trace opts)
  (let [api-handler (some-> handlers :api :handler)
        web-handler (some-> handlers :web :handler)
        api-middlewares (or (some-> handlers :api :middlewares)
                            identity)
        web-middlewares (or (some-> handlers :web :middlewares)
                            identity)
        handler (fn [req]
                  (or (and api-handler ((api-middlewares api-handler) req))
                      (and web-handler ((web-middlewares web-handler) req))))]
    (-> opts
        (assoc-in [:handlers :main] handler)
        (assoc :server (https/run-server handler opts)))))

(defmethod ig/halt-key! :{{name}}/http-server [_ {:keys [server stop-timeout] :as opts}]
  (info "stopping {{name}}/http-server" opts)
  (server :timeout stop-timeout))
