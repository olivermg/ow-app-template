(ns {{name}}.http-web
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [clojure.string :as str]))

(defmethod ig/init-key :{{name}}/http-web [_ {:keys [prefix] :as opts}]
  (info "starting {{name}}/http-web handler")
  (trace opts)
  (fn [{:keys [uri] :as req}]
    (when (and uri (str/starts-with? uri prefix))
      (warn "web request" req)
      {:status 200
       :body "web response"})))
