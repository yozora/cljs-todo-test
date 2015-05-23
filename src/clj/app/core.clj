(ns app.core
  [:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET POST defroutes]]])

(defroutes app-routes
  (GET "/" [] "Nothing Here.")
  (route/resources "/")
  (route/resources "/react" {:root "react"}))

(def app
  (handler/api app-routes))
