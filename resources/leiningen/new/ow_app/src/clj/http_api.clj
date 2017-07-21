(ns {{name}}.http-api
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [compojure.api.sweet :as cas]
            [liberator.core :as l]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults secure-api-defaults]]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::name string?)
(s/def ::greeting string?)
(s/def ::age int?)

(defn- as-response [data lib-ctx]
  {:status (:status lib-ctx)
   :body data})

(defonce lib-default-config
  {:available-media-types ["application/transit+json" "application/json"]
   :respond-with-entity?  true
   :can-post-to-missing?  true
   :can-put-to-missing?   false
   :new?                  false
   :as-response           as-response})

(defonce lib-authorized-config
  (merge lib-default-config
         {:authorized? (fn [{:keys [request] :as lib-ctx}]
                         ;;; TODO: ...check auth...
                         {:auth {:user "apiuser1"}})}))

(defonce ca-default-config
  {:consumes ["application/transit+json" "application/json"]
   :produces ["application/transit+json" "application/json"]})

(def hello-lib
  (l/resource
   lib-authorized-config

   :allowed-methods [:get :post]

   :exists? (fn [{:keys [request auth] :as lib-ctx}]
              ;;; TODO: ...check if requested entity exists...
              (debug "exists?")
              {:hello (str "hello " (get-in request [:params :name]))})

   :post! (fn [{:keys [request hello] :as lib-ctx}]
            ;;; TODO: ...store data...
            (debug "post!")
            {:hello (str "posted " (get-in request [:params :age]) " " hello)})

   :handle-ok (fn [{:keys [request hello] :as lib-ctx}]
                ;;; TODO: ...retrieve data...
                (debug "handle-ok")
                {:greeting hello})))

(def hello
  (cas/context "/hello/:name" []
    :path-params [name :- ::name]

    (cas/resource
     (merge ca-default-config
            {:tags ["example"]
             :get {:responses {200 {:schema (s/keys :req-un [::greeting])}}
                   :summary "get hello"}
             :post {:responses {200 {:schema (s/keys :req-un [::greeting])}}
                    :parameters {:body-params (s/keys :req-un [::age])}
                    :summary "post hello"}
             :handler hello-lib}))))

(defn make-handler [prefix]
  (cas/context prefix []
    (cas/api
     {:coercion :spec}
     hello)))

(defn wrap-middlewares [handler]
  (-> handler
      (wrap-defaults api-defaults)))

(defmethod ig/init-key :{{name}}/http-api [_ {:keys [prefix] :as opts}]
  (info "starting {{name}}/http-api handler")
  (trace opts)
  {:handler     (make-handler prefix)
   :middlewares wrap-middlewares})
