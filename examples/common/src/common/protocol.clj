(ns common.protocol)

(defprotocol IGeometry
  (area [this])
  (perim [this]))


(defrecord Square [width height]
  IGeometry
  (area [this]
    (* width height))

  (perim [this]
    (+ (* 2 width) (* 2 height))))


(defrecord Circle [radius]
  IGeometry
  (area [this]
    (* Math/PI radius radius))

  (perim [this]
    (* 2 Math/PI radius)))




(defn measure [g]
  (println g)
  (println (area g))
  (println (perim g)))

(measure (map->Square {:width 3, :height 4}))
;>> #common.protocol.Square{:width 3, :height 4}
;>> 12
;>> 14
;=> nil


(measure (map->Circle {:radius 5}))
;>> #common.protocol.Circle{:radius 5}
;>> 78.53981633974483
;>> 31.41592653589793
;=> nil
