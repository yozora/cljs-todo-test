(ns todo-mvc.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
			[clojure.string :as string]
            [cljs.core.async :refer [put! <! chan]]
            [sablono.core :as html :refer-macros [html]]
            [om.dom :as dom])
  (:import [goog.ui IdGenerator]))

(enable-console-print!)

(defonce app-state (atom {
  :showing :all
  :todos [] }))

(defn L [input]
  (get {:Todos "Todos" } input input))

(defn guid []
  (.getNextUniqueId (.getInstance IdGenerator)))

;; Handlers

(defn destroy-todo [app {:keys [id]}]
  (om/transact! app :todos
    (fn [todos] (into [] (remove #(= (:id %) id) todos))))
)

(defn handle-message [{:keys [type data] :as message} app]
  (case type
    :destroy (destroy-todo app data)
    nil))
    
(def ENTER_KEY 13)

(defn handle-entry-keydown [e app owner]
  (when (== (.-which e) ENTER_KEY)
    (let [todo-field (om/get-node owner "todo-text")
          text (.-value todo-field)]
      (when-not (string/blank? text)
        (let [newtodo {:id (guid) :text text :completed false}]
          (om/transact! app :todos #(conj % newtodo)
          (set! (.-value todo-field) "")))))))

;; Components

(defn todo-list-item [item owner]
  (reify
    om/IDisplayName
    (display-name [_] "todo-list-item")
    om/IRenderState
    (render-state [_ {:keys [comm]}] (html
      [:li {:className (if (:completed item) "completed" "")}
        [:div {:className "view"}
          [:input {:type "checkbox"
                   :className "toggle"
                   :on-change #(om/transact! item :completed not)
                   :checked (and (:completed item) "checked")}]
          [:label (:text item)]
          [:button {:className "destroy"
   					:on-click (fn [_] (put! comm {:type :destroy :data item}) nil)}]]]))))

(defn top-app [{:keys [todos] :as app} owner]
  (reify
    om/IDisplayName
    (display-name [_] "top-app")
    om/IRenderState
    (render-state [_ {:keys [comm]}] (html
      [:section {:id "top-app" :style (if (empty? todos) {:display "none"} {}) }
        [:input {:id "toggle-all" :type "checkbox"}]
        (into
          [:ul {:id "todo-list"}]
          (om/build-all todo-list-item todos {:init-state {:comm comm} :key :id}))]))))

(defn todo-app [{:keys [todos] :as app} owner]
  (reify
    om/IDisplayName
    (display-name [_] "todo-app")
    om/IWillMount
    (will-mount [_]
      (let [comm (chan)]
      	(om/set-state! owner :comm comm)
      	(go (while true
      	  (let [message (<! comm)]
      	  	(handle-message message app))))))
    om/IRenderState
    (render-state [_ {:keys [comm]}] (html
      [:div
        [:header {:id "header"}
          [:h1 (L :Todos)]
          [:input { :id "new-todo"
                    :ref "todo-text"
                    :onKeyDown #(handle-entry-keydown % app owner)
                    :placeholder (L "What needs to be done?")}]]
        (om/build top-app app {:init-state {:comm comm}})]))
))

(defn main []
    (om/root
      todo-app
      app-state
      {:target (.getElementById js/document "todoapp")}))
