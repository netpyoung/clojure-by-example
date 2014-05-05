<!DOCTYPE html>
<html><head><meta charset="utf-8"><link href="../../css/style.css" rel="stylesheet"><link href="http://yandex.st/highlightjs/8.0/styles/default.min.css" rel="stylesheet"><script src="http://yandex.st/highlightjs/8.0/highlight.min.js"></script><script src="http://yandex.st/highlightjs/8.0/languages/clojure.min.js"></script><script>hljs.initHighlightingOnLoad();</script></head><body><div class="example" id="alts"><h2><a href="http://www.google.com">google!?</a>:async.alts</h2></div><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(ns async.alts)</code></pre></td></tr></table><table><tr><td class="docs"><p>https://gobyexample.com/select</p>
</td><td class="code"><pre><code class="clojure"></code></pre></td></tr></table><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(require '[clojure.core.async :as async :refer [&lt;! &gt;! &lt;!! timeout chan alt! go go-loop put! alts! alts!!]])</code></pre></td></tr></table><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(def c1 (chan))
(def c2 (chan))</code></pre></td></tr></table><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(go (&lt;! (timeout 1000))
    (&gt;! c1 &quot;one&quot;))</code></pre></td></tr></table><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(go (&lt;! (timeout (* 2 1000)))
    (&gt;! c2 &quot;two&quot;))</code></pre></td></tr></table><table><tr><td class="docs"></td><td class="code"><pre><code class="clojure">(dotimes [i 2]
  (let [[v ch] (alts!! [c1 c2])]
    (condp = ch
      c1 (println &quot;received&quot; v)
      c2 (println &quot;received&quot; v)
      nil)))</code></pre></td></tr></table></body></html>