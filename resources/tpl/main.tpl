{% extends "tpl/default.tpl" %}
{% block contents %}
<div class="main">
  <ul>
    {% for item in items %}
    <li>{{item|safe}}</li>
    {% endfor %}
  </ul>
</div>
<!--
main-page
main-index-page
section-index-page
example-page
-->

{% endblock %}
