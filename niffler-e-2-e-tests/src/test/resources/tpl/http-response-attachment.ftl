<#ftl output_format="HTML">
<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<head>
    <meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <link type="text/css" href="https://yandex.st/highlightjs/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/highlight.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/json.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/xml.min.js"></script>
    <script type="text/javascript" src="https://yandex.st/highlightjs/8.0/languages/bash.js"></script>
    <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
            white-space: pre-wrap;
            margin: 15px 0;
            padding: 15px;
            border-radius: 5px;
            background-color: #f5f5f5;
        }

        .content-block {
            margin: 30px 0;
            padding: 20px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        h4 {
            color: #2c3e50;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #ecf0f1;
        }

        body {
            padding: 20px;
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }
    </style>
</head>
<body>
<div class="content-block">
    <h4>Status code</h4>
    <#if data.responseCode??>${data.responseCode} <#else>Unknown</#if>
    <#if data.url??>${data.url}</#if>
</div>

<#if data.body??>
    <div class="content-block">
        <h4>Body</h4>
        <div>
            <pre><code><#t>${data.body}</code></pre>
        </div>
    </div>
</#if>

<#if (data.headers)?has_content>
    <div class="content-block">
        <h4>Headers</h4>
        <div>
            <#list data.headers as name, value>
                <pre><code>${name}: ${value!"null"}</code></pre>
            </#list>
        </div>
    </div>
</#if>

<#if (data.cookies)?has_content>
    <div class="content-block">
        <h4>Cookies</h4>
        <div>
            <#list data.cookies as name, value>
                <pre><code>${name}: ${value!"null"}</code></pre>
            </#list>
        </div>
    </div>
</#if>
