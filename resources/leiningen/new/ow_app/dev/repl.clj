(ns repl
  (:require [cemerick.piggieback :as p]
            [figwheel-sidecar.repl-api :as f]))

(defonce http-handler nil)

(defn start-figwheel []
  (f/start-figwheel! "browser-dev"))

(defn stop-figwheel []
  (f/stop-figwheel!))

(defn start-browser-repl []
  (f/cljs-repl))
