(ns app.core
  (:require [om.core :as om :include-macros true]
            [figwheel.client :as figwheel :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om.dom :as dom]
            [todo-mvc.core :as todo]))

(enable-console-print!)

(defonce app-state (atom {}))

(defn main []
  (. js/console (log "Hello World!"))
  (todo/main))

(figwheel/watch-and-reload
  :websocket-url "ws://192.168.10.100:3449/figwheel-ws"
  :jsload-callback (fn []
                     (main)))

(main)
