<%--
~ Copyright 2012 Stephen Connolly
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
--%>

<%@ attribute name="id" required="true" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="org.apache.commons.io.IOUtils" %>
<%@ tag import="org.jsoup.Jsoup" %>
<%@ tag import="org.jsoup.nodes.Document" %>
<%@ tag import="org.tobarsegais.webapp.ServletContextListenerImpl" %>
<%@ tag import="java.io.InputStream" %>
<%@ tag import="java.net.JarURLConnection" %>
<%@ tag import="java.net.URL" %>
<%@ tag import="java.net.URLConnection" %>
<%@ tag import="java.util.Map" %>
<%@ tag import="java.util.jar.JarEntry" %>
<%@ tag import="java.util.jar.JarFile" %>
<div id="${id}"><%
    String path = (String) request.getAttribute("content");
    Map<String, String> bundles = ServletContextListenerImpl.getBundles(application);
    boolean found = false;
    for (int index = path.indexOf('/'); index != -1; index = path.indexOf('/', index + 1)) {
        String key = path.substring(0, index);
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        if (bundles.containsKey(key)) {
            key = bundles.get(key);
        }
        URL resource = application.getResource(ServletContextListenerImpl.BUNDLE_PATH + "/" + key + ".jar");
        if (resource == null) {
            continue;
        }
        URL jarResource = new URL("jar:" + resource + "!/");
        URLConnection connection = jarResource.openConnection();
        if (!(connection instanceof JarURLConnection)) {
            continue;
        }
        JarURLConnection jarConnection = (JarURLConnection) connection;
        JarFile jarFile = jarConnection.getJarFile();

        int endOfFileName = path.indexOf('#', index);
        endOfFileName = endOfFileName == -1 ? path.length() : endOfFileName;
        String fileName = path.substring(index + 1, endOfFileName);
        JarEntry jarEntry = jarFile.getJarEntry(fileName);
        if (jarEntry == null) {
            continue;
        }
        InputStream in = null;
        try {
            in = jarFile.getInputStream(jarEntry);
            Document document = Jsoup.parse(in, "UTF-8", request.getRequestURI());
            document.select("link").remove();
            document.select("script").remove();
            out.print(document.body());
            found = true;
            break;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    if (!found) {
        %><%@include file="/WEB-INF/default-help.jspf"%><%
    }

%></div>
