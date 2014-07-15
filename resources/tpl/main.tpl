{% extends "tpl/default.tpl" %}

{% block contents %}
<div class="main">
  <h1><a href="public">public</a></h1>

  <ul>
    {% for item in items %}
    <li>
      <a href="{{item.link}}">{% if item.title %}{{item.title}}{% endif %}{% if item.img %}<img src="{{item.img}}" />{% endif %}</a>
    </li>
    {% endfor %}
  </ul>
</div>
{% endblock %}
