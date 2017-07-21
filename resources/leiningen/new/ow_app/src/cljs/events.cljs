(ns {{name}}.events
  (:require [re-frame.core :refer [reg-event-db
                                   reg-event-fx
                                   reg-fx
                                   reg-cofx
                                   inject-cofx]]))

(reg-event-db :foo-event
              (fn [db msg]
                (println "msg:" msg)
                db))
