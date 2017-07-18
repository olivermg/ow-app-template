(ns {{name}}.http-api
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [clojure.string :as str]))

(defmethod ig/init-key :{{name}}/http-api [_ {:keys [prefix] :as opts}]
  (info "starting {{name}}/http-api handler")
  (trace opts)
  (fn [{:keys [uri] :as req}]
    (when (and uri (str/starts-with? uri prefix))
      (warn "api request" req)
      {:status 200
       :body "api response"})))
