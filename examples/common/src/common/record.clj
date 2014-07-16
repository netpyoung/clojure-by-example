(ns common.record)

(defrecord Person [name age])

(Person. "Bob" 20)
;=> #common.record.Person{:name "Bob", :age 20}


(map->Person {:name "Alice", :age 30})
;=> #common.record.Person{:name "Alice", :age 30}


(map->Person {:name "Fred"})
;=> #common.record.Person{:name "Fred", :age nil}


(def s (map->Person {:name "Sean", :age 50}))

(:name s)
;=> "Sean"

(:age s)
;=> 50


(assoc s :age 51)
;=> #common.record.Person{:name "Sean", :age 51}

(println s)
;>> #common.record.Person{:name Sean, :age 50}
;=> nil
