function initAcordionTabs(ccid) {
    var cid = PrimeFaces.escapeClientId(ccid);

    jQuery( cid + " .accordion.hided").parent().next().hide();

    //toggle the componenet with class msg_body
    jQuery( cid + " .accordion:not(.inited) .accordionLabel, " + cid + " .accordion:not(.inited) .accordionIcon, " + cid + " .accordion:not(.inited) .accordionIcon2").click(function()
    {
        var parent = jQuery(this).parent().parent();
        parent.next(".accordioncontent").slideToggle(500);
        var icon = parent.children(".accordionIcon");
        if (icon.hasClass("fa-caret-right")) {
            icon.removeClass("fa-caret-right");
            icon.addClass("fa-caret-down");
        } else {
            icon.removeClass("fa-caret-down");
            icon.addClass("fa-caret-right");
        }
    });

    jQuery( cid + ".accordion").addClass("inited");
}