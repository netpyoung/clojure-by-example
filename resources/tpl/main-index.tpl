{% extends "tpl/default.tpl" %}
{% block contents %}

<div class="main">
  {% for section in sections %}
  <div id="{{section}}">
    <h3><a href="{{section}}">{{section|capitalize}}</a></h3>
  </div>
  {% endfor %}
</div>

{% endblock %}
