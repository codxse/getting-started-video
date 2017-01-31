(ns app.ui
  (:require [om.dom :as dom]
            [om.next :as om :refer-macros [defui]]
            yahoo.intl-messageformat-with-locales
            [untangled.client.core :as uc]))


(defui ^:once MyItem
  static uc/InitialAppState
  (initial-state [this {:keys [label]}] {:label label})
  static om/IQuery
  (query [this] [:label])
  static om/Ident
  (ident [this {:keys [label]}] [:items/by-label label])
  Object
  (render [this]
    (let [{:keys [label]} (om/props this)]
      (dom/li nil label))))

(def ui-item (om/factory MyItem {:keyfn :label}))

(defui ^:once MyList
  static uc/InitialAppState
  (initial-state [this params]
    {:title "Initial List"
     :items [(uc/initial-state MyItem {:label "A"})
             (uc/initial-state MyItem {:label "B"})
             (uc/initial-state MyItem {:label "C"})]})
  static om/IQuery
  (query [this] [:title :items])
  Object
  (render [this]
    (let [{:keys [title items]} (om/props this)]
      (dom/div nil
        (dom/h4 nil title)
        (dom/ul nil
          (map ui-item items))))))

(def ui-list (om/factory MyList))

(defui ^:once Root
  static uc/InitialAppState
  (initial-state [this params] {:list (uc/initial-state MyList {})})
  static om/IQuery
  (query [this] [:ui/react-key {:list (om/get-query MyList)}])
  Object
  (render [this]
    (let [{:keys [ui/react-key list]} (om/props this)]
      (dom/div #js {:key react-key}
        (dom/h4 nil "Header")
        (ui-list list)))))