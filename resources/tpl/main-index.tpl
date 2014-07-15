{% extends "tpl/default.tpl" %}
{% block contents %}

<div class="main">
  {% for item in items %}
  <div id="{{item.section}}">
    <h3><a href="{{item.section}}">{{item.section|capitalize}}</a></h3>
  </div>
  {% endfor %}
</div>

{% endblock %}
