(ns repl
  (:require [cemerick.piggieback :as p]
            [figwheel-sidecar.repl-api :as f]
            [ring.middleware.reload :refer [wrap-reload]]
            [{{name}}.core :as c]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defonce system (atom nil))
(defonce ring-handler nil)
(defonce less (atom nil))

(defn start-system []
  (reset! system (c/start))
  (alter-var-root #'ring-handler
                  (fn [v]
                    (wrap-reload
                     (-> @system :{{name}}/http-server :handlers :main)))))

(defn stop-system []
  (c/stop @system))

(defn start-figwheel []
  (f/start-figwheel! "browser-dev"))

(defn stop-figwheel []
  (f/stop-figwheel!))

(defn start-less []
  (reset! less (future (clojure.java.shell/sh "lein" "less" "auto"))))

(defn stop-less []
  (future-cancel @less) ;; FIXME: this does not work yet
  (reset! less nil))

(defn start-browser-repl []
  (f/cljs-repl))

(defn start []
  (pcalls start-system
          start-figwheel
          start-less))

(defn stop []
  (pcalls stop-less
          stop-figwheel
          stop-system))
