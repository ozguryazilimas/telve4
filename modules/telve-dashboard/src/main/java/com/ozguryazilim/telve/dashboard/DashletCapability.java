/*
 * Copyleft 2007-2010 Uygun Teknoloji
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * www.uygunteknoloji.com.tr
 */

package com.ozguryazilim.telve.dashboard;

/**
 * Dashlet Capabilities
 * @author Hakan Uygun
 */
public enum DashletCapability {
    canHide,
    canMinimize,
    canMaximize,
    canExecute,
    canRefresh,
    canEdit,
    isSystem,
    isMandetory,
    canMultiInstance,
    hasIcon,
    hasCaptionHandler,
    needInit
}
