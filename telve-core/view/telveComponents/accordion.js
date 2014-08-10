function initAcordionTabs(ccid) {
    var cid = PrimeFaces.escapeClientId(ccid);

    jQuery( cid + " .accordion.hided").next().hide();

    //toggle the componenet with class msg_body
    jQuery( cid + " .accordion:not(.inited) .accordionLabel, " + cid + " .accordion:not(.inited) .accordionIcon, " + cid + " .accordion:not(.inited) .accordionIcon2").click(function()
    {
        var parent = jQuery(this).parent();
        parent.next(".accordioncontent").slideToggle(500);
        var icon = parent.children(".accordionIcon");
        if (icon.hasClass("ui-icon-triangle-1-e")) {
            icon.removeClass("ui-icon-triangle-1-e");
            icon.addClass("ui-icon-triangle-1-s");
        } else {
            icon.removeClass("ui-icon-triangle-1-s");
            icon.addClass("ui-icon-triangle-1-e");
        }
    });

    jQuery( cid + ".accordion").addClass("inited");
}