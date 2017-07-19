(ns {{name}}.core
  (:gen-class)
  (:require [taoensso.timbre :refer [trace debug info warn error fatal] :as tl]
            [taoensso.timbre.tools.logging :as tlt]
            [integrant.core :as ig]
            [signal.handler :as sig]
            [environ.core :refer [env]]
            [{{name}}.system :as sys]))

(defn- make-logging-config []
  (tlt/use-timbre) ;; make clojure.tools.logging use timbre
  (let [pla (-> (tl/println-appender)
                (assoc-in [:timestamp-opts :pattern] "yyyy-MM-dd HH:mm:ss.SSS"))]
    (-> tl/example-config
        (assoc-in [:level] (keyword (or (env :loglevel) "info")))
        (assoc-in [:appenders] {:println pla}))))

(defn start []
  (tl/with-config (make-logging-config)
    (ig/load-namespaces sys/config)
    (ig/init sys/config)))

(defn stop [s]
  (ig/halt! s))

(defn -main [& args]
  (let [s (atom (start))]
    (sig/with-handler :int
      (warn "caught INT, quitting...")
      (ig/halt! @s))
    (sig/with-handler :term
      (warn "caught TERM, quitting...")
      (ig/halt! @s))
    (sig/with-handler :hup
      (warn "caught HUP, restarting...")
      (ig/halt! @s)
      (reset! s (start)))))
