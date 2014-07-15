{% extends "tpl/default.tpl" %}
{% block contents %}

<div class="main">
  <h1><a href="../">public</a></h1>
  <h2>{{section}}</h2>
    <ul>
      {% for item in items %}
      <li><a href="{{item.basename}}">{{item.title}}</a></li>
      {% endfor %}
    </ul>
</div>

{% endblock %}
