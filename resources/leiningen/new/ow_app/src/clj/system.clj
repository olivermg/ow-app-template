(ns {{name}}.system
  (:require [integrant.core :as ig]
            [environ.core :refer [env]]))

(def config
  {:{{name}}/http-server {:port         (Integer. (or (env :port) "8090"))
                          :stop-timeout (Integer. (or (env :stop-timeout) "10000"))
                          :handlers     {:api (ig/ref :{{name}}/http-api)
                                         :web (ig/ref :{{name}}/http-web)}}
   :{{name}}/http-api {:prefix "/api"}
   :{{name}}/http-web {:prefix "/web"}})
