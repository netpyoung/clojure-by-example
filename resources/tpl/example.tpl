{% extends "tpl/default.tpl" %}
{% block head %}
<link ref="stylesheet" href="http://yandex.st/highlightjs/8.0/styles/default.min.css" />
<script src="http://yandex.st/highlightjs/8.0/highlight.min.js"></script>
<script src="http://yandex.st/highlightjs/8.0/languages/clojure.min.js"></script>
<script>hljs.initHighlightingOnLoad();</script>
{% endblock %}


{% block contents %}
<div class="example-header">
  <h2><a href="./">{{parent-dir}}</a></h2>
  <h3><a href="https://github.com/netpyoung/clojure-by-example/blob/gh-pages/examples/src/{{cljfname}}">src</a></h3>
  <h3><a href="https://raw.githubusercontent.com/netpyoung/clojure-by-example/gh-pages/examples/src/{{cljfname}}">raw</a></h3>
</div>

<div class="example">
  {% for doctable in doctables %}
  <table>
    <tr>
      <td class="docs">
        {{doctable.docs|safe}}
      </td>

      <td class="code">
        <pre><code class="clojure">{{doctable.code|safe}}</code></pre>
      </td>
    </tr>
  </table>
  {% endfor %}
</div>
{% endblock %}
