<html xmlns="http://www.w3.org/1999/html">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
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
        }
    </style>
</head>
<body>
<h4>Request</h4>
<div>
    <pre><code><#t><#if data.method??>${data.method}<#else>GET</#if> to <#if data.url??>${data.url}<#else>Unknown</#if></code></pre>
</div>

<#if data.body??>
    <h4>Body</h4>
    <div>
        <pre><code><#t>${data.body}</code></pre>
    </div>
</#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
    <div>
        <#list data.headers as name, value>
            <pre><code>${name}: ${value!"null"}</code></pre>
        </#list>
    </div>
</#if>

<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
    <div>
        <#list data.cookies as name, value>
            <pre><code>${name}: ${value!"null"}</code></pre>
        </#list>
    </div>
</#if>

<#if data.curl??>
    <h4>Curl</h4>
    <div>
        <pre><code>${data.curl}</code></pre>
    </div>
</#if>

<#if (data.formParams)?has_content>
<h4>FormParams</h4>
<div>
    <#list data.formParams as name, value>
        <pre><code>${name}: ${value!"null"}</code></pre>
    </#list>
</div>
</#if>