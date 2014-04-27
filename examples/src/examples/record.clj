(ns examples.record)

(defrecord Person [name age])

(Person. "Bob" 20)
;=> #examples.record.Person{:name "Bob", :age 20}


(map->Person {:name "Alice", :age 30})
;=> #examples.record.Person{:name "Alice", :age 30}


(map->Person {:name "Fred"})
;=> #examples.record.Person{:name "Fred", :age nil}


(def s (map->Person {:name "Sean", :age 50}))

(:name s)
;=> "Sean"

(:age s)
;=> 50


(assoc s :age 51)
;=> #examples.record.Person{:name "Sean", :age 51}

(println s)
;>> #examples.record.Person{:name Sean, :age 50}
;=> nil
