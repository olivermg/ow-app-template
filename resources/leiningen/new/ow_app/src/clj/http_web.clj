(ns {{name}}.http-web
  (:require [taoensso.timbre :refer [trace debug info warn error fatal]]
            [integrant.core :as ig]
            [liberator.core :as l]
            [ataraxy.core :as a]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults secure-site-defaults]]
            [clojure.string :as str]))

(def routes
  '{["/hello/" name] [:hello name]})

(defonce lib-default-config
  {:available-media-types ["text/html"]})

(defonce lib-authorized-config
  (merge lib-default-config
         {:authorized? (fn [{:keys [request] :as lib-ctx}]
                         ;;; TODO: ...check auth...
                         {:auth {:user "webuser1"}})}))

(defmacro resource [& body]
  `(fn [req#]
     ((l/resource ~@body) (update req# :params #(merge % (:route-params req#))))))

(def hello
  (resource
   lib-authorized-config

   :allowed-methods [:get :post]

   :exists? (fn [{:keys [request] :as lib-ctx}]
              ;;; TODO: ...check if resource exists...
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

(defn make-handler [prefix]
  (let [routes {prefix routes}]
    (a/handler
     {:routes routes
      :handlers {:hello hello}})))

(defn wrap-middlewares [handler]
  (-> handler
      (wrap-defaults site-defaults)))

(defmethod ig/init-key :{{name}}/http-web [_ {:keys [prefix] :as opts}]
  (info "starting {{name}}/http-web handler")
  (trace opts)
  {:handler     (make-handler prefix)
   :middlewares wrap-middlewares})
