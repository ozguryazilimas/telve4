function initAcordionTabs(ccid) {
    var cid = PrimeFaces.escapeClientId(ccid);

    jQuery( cid + ".accordion.hided .accordioncontent").hide();

    //toggle the componenet with class msg_body
    //jQuery( cid + ".accordion:not(.inited) .accordionLabel, " + cid + ".accordion:not(.inited) .accordionIcon, " + cid + ".accordion:not(.inited) .accordionIcon2").click(function()
    jQuery( cid + ".accordion:not(.inited) .accordion-title").click(function()
    {
        var parent = jQuery(this).parent(); //.parent();
        parent.next(".accordioncontent").slideToggle(500);
        var icon = parent.children('.accordion-title').children('.accordionIcon');
        if (icon.hasClass("fa-caret-right")) {
            icon.removeClass("fa-caret-right");
            icon.addClass("fa-caret-down");
            //localStorage.setItem('acr-'+ cid + window.location.pathname, 'true');
            localStorage.removeItem('acr-'+ cid + window.location.pathname);
        } else {
            icon.removeClass("fa-caret-down");
            icon.addClass("fa-caret-right");
            localStorage.setItem('acr-'+cid + window.location.pathname, 'false');
        }
    });

    jQuery( cid + ".accordion").addClass("inited");
    var st = localStorage.getItem('acr-' + cid + window.location.pathname);
    if( st == 'false'){
        jQuery( cid + ".accordion .accordioncontent").hide();
        jQuery( cid + ".accordion .accordionIcon").removeClass("fa-caret-down");
        jQuery( cid + ".accordion .accordionIcon").addClass("fa-caret-right");
    }
}