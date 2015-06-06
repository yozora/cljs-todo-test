(ns app.core
  [:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET POST defroutes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as json-middleware]])

(defroutes app-routes
  (GET "/" [] "Nothing Here.")
  (POST "/echo" [method params] {:body {:result (get params 0) :id 0}})
  (route/resources "/")
  (route/resources "/react" {:root "react"}))

(def app
  (-> app-routes
      (json-middleware/wrap-json-params)
      (json-middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
  ;(handler/api app-routes))
