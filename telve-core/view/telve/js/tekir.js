var oldloader;

var retpointStack = new Array();

function init(){
    //focusFirstField();
    //oldloader();
}

function toggleActionIcons(elementId) {
    var elem = $(elementId);
    if (elem.style.display=='') {
        elem.style.display='none';
    } else {
        elem.style.display='';
    }
}



function isEnterCode(event) {
    if (event.keyCode==13) {
        //		alert('u pressed enter');
        msjs();
        return false;
    }
}

function disableEnterCode(event) {
    if (event.keyCode==13) {
        return false;
    }
}

function submitOnEnterPressed(event, buttonId) {
    if (event.keyCode==13) {
        document.getElementById(buttonId).click();
    	return false;
    }
}

function clearBarcodeInput() {
    var barcodeBox = $('barcodeInclude:barcodeForm:barcodeBox');
    barcodeBox.value = '';
    barcodeBox.focus();
}

function focusToBarcodeInput() {
    var barcodeBox = $('barcodeInclude:barcodeForm:barcodeBox');
    barcodeBox.focus();
}

function focusFirstField(){
    
    var ff = jQuery( '.utff' );
	
    if( ff.length > 0 ){
        var f = ff[ ( ff.length - 1 ) ];
        Form.Element.focus(f);
        return;
    }
    
    var form = $('form');
    if( form != null ){
        Form.focusFirstElement( form );
    }
}
  
function focusLastRow(){

    var ee = jQuery( '.utdff' );
    
    if( ee.length > 0 ){
        var e = ee[ ( ee.length - 1 ) ];
        Form.Element.focus(e);
    }
    
}

function focusToField(elementId){

    var elem = $( elementId );
    if ( elem != null ){
    	elem.focus();
    }
    
}

// personel ekleme sayfasinda pasaport ve tckimlik numarası kontrolu
function tcnoPersonelNoBosmu(){
    var tcno=$('form:tcno:tckimlikno').value;
    var passportNo=$('form:passport:passportNo').value;
	
    if(tcno == '' && passportNo == ''){
        alert('Pasaport No veya TC Kimlik No degerlerinden en az bir tanesinin girilmesi zorunludur');
        return false;
    }
    //eger ikisinden biri girilmisse boş alan kontrolu yap.
    return checkReq();
}

/**
 * Bun fonksiyon verilen değeri verilen elemente koyar ve focusu o elemente taşır...
 */
function selectValue( retpoint, val ){
    var elem = $( retpoint );
    if (elem != null) {
        if( val != '' ){
            elem.value = val;
        }
        elem.focus();
    }
}

/**
 * Bu fonksiyon ile Modal Panel açılırken alnınan geri dönüş id'si form üzerinde gizli bir alana yazılır.
 */
function saveReturnPoint( retpoint ){
    retpointStack.push( retpoint );
}

function saveContactReturnPoint( retpoint ){
	//TODO: Buranın da temizlenmesi lazım
    var elem = $('retcontact');
    elem.value = retpoint;
}
 
 
function deleteConfirmation(){
    return !confirm('Kaydı silmek istediğinizden emin misiniz?');
}
 
 
function openHelp(){
    newwin = window.open(null, 'HelpWinId', 'width=850,height=600,scrollbars=yes,resizable=yes,status=no,toolbar=no,location=no,menubar=no,directories=no')
    if (window.focus) {
        newwin.focus()
        }

}

 
//oldloader = window.onload;
//window.onload = init();
window.setTimeout( 'focusFirstField()', 100 );

function toggleCheckAll(){
    jQuery(":checkbox").not(":disabled").each(function() {
        this.checked = !$("form:browseList:checkAll").checked;
    });
    $("form:browseList:checkAll").checked = !$("form:browseList:checkAll").checked;
}

function closeActivePanel() {
    topPanel = ModalPanel.activePanels.last();
    if (topPanel != null) {
        topPanel.hide();
    }
}

document.onkeydown = function(e){
    if (e == null) { // ie
        keycode = event.keyCode;
    } else { // mozilla
        keycode = e.which;
    }
    if(keycode == 27){ // escape, close box
        closeActivePanel();
    }
}


function stopEventBubling(e){
    e.stopPropagation();
}
/**
 * Toogle edilecek olan row verilir. Aynı tbody altındaki rowları gizler ya da açar.
 */
function toggleTableRows(e){

    //e : bir <a> üstünde bir td onun üstünde bir tr onunda üstünde bir tbody
    jQuery("tr.child", jQuery(e).parent().parent().parent()).slideToggle("fast");
//jQuery(e).toggleClass("collapsed");

/*
$(document).ready(function() {

   $("tr.header").click(function () {
      $("tr.child", $(this).parent()).slideToggle("fast");
   });


});
*/
}


var expandTable = {
    init: function(tableid){
        jQuery(".toggleCell").each(function(){
            jQuery('<a href="#" class="toggler">Toogler</a>').mousedown(function (e) {
                /* STOP event bubbling */
                e.stopPropagation();
            }).click(function(){
                if( jQuery(this).hasClass("collapsed") ){
                    jQuery(this).removeClass("collapsed");
                    jQuery("tr.child", jQuery(this).parent().parent().parent()).show();
                } else {
                    jQuery(this).addClass("collapsed");
                    jQuery("tr.child", jQuery(this).parent().parent().parent()).hide();
                }
                return false;
            }).prependTo(jQuery(this));
        });
    }
}


function initRoleTable(){
    jQuery("#permissionTable th").css("text-align","left");
    jQuery("#permissionTable td").css("text-align","left");
    jQuery("#permissionTable .checkAll").click(function(){
        var v = this.checked;
        var s = jQuery(this).attr("id");
        var ss = s.split(":")[1];
        jQuery(".p"+ss+":checkbox").each(function() {
            this.checked = v;
        });
    });
    jQuery("#permissionTable .checkGroup").click(function(){
        var v = this.checked;
        var s = jQuery(this).attr("id");
        var ss = s.split(":")[1];
        var pgs = s.split(":")[2];
        jQuery(".g" + ss + pgs + ":checkbox").each(function() {
            this.checked = v;
        });
    });
    jQuery("#permissionTable .checkOther").click(function(){
        var v = this.checked;
        var s = jQuery(this).attr("id");
        var g = s.split(":")[1];
        var r = s.split(":")[2];
        jQuery(".go" + g + r + ":checkbox").each(function() {
            this.checked = v;
        });
    });
    //Diger Haklar kolonunu kapatıp acar
    jQuery(".oatoggler").each(function(){
            jQuery('<a href="#" class="toggler">Toogler</a>').mousedown(function (e) {
                /* STOP event bubbling */
                e.stopPropagation();
            }).click(function(){
                if( jQuery(this).hasClass("collapsed") ){
                    jQuery(this).removeClass("collapsed");
                    jQuery(".oatable", jQuery(this).parent().parent()).show();
                } else {
                    jQuery(this).addClass("collapsed");
                    jQuery(".oatable", jQuery(this).parent().parent()).hide();
                }
                return false;
            }).prependTo(jQuery(this));
        });
}

function initTreeHomeJSTree( treeId, conversationId, provider, contextPath){
	jQuery(function () {
		jQuery( treeId ).jstree({
			 		"core" : { "html_titles" : true },
					"themes" : {
				        "theme" : "classic",
					},
			        "json_data" : {
			            "ajax" : { 
			            	"url" : contextPath + "/popup/treeHomeData.seam" ,
			            		"data" : function (n) {
			            		// the result is fed to the AJAX request `data` option
			            		return {
			            			"operation" : "get_children",
			            			"parentId"  : n.attr ? n.attr("id").replace("node_","") : 0,
					            	"cid"       : conversationId,
					            	"provider"	: provider
			            		};
			            	}
			            }
			        },
			      	"cookies" : { "save_selected" : false },
			        "plugins" : [ "themes", "json_data", "ui", "cookies" ]
			    }).bind("select_node.jstree", function (e, data) { 
			    		selectNode([{name:"selectedNodeId", value:data.rslt.obj[0].id.replace("node_","")}]);
				});
			});
}

function initTreeSelectJSTree( conversationId, provider, contexPath ){
	jQuery(function () {
		jQuery("#" + provider + "selectTree").jstree({
					"core" : { "html_titles" : true },
					"themes" : {
				        "theme" : "classic",
					},
			        "json_data" : {
			            "ajax" : { 
			            	"url" : contexPath + "/popup/treeSelectData.seam" ,
			            		"data" : function (n) {
			            		// the result is fed to the AJAX request `data` option
			            		return {
			            			"operation" : "get_children",
			            			"parentId" : n.attr ? n.attr("id").replace("node_","") : 0,
					            	"cid" : conversationId,
					            	"provider" : provider
			            		};
			            	}
			            }
			        },
			        "cookies" : { "save_selected" : false },
			        "plugins" : [ "themes", "json_data", "ui", "cookies" ]
			    }).bind("select_node.jstree", function (e, data) { 
					//Herhangi birimi yoksa sadece leafler mi seçilebilir?
					var sm = jQuery("#selectMode").val();
			    	if( "any" == sm || jQuery( data.rslt.obj[0] ).hasClass('jstree-none') ){
				    		//closeTreeSelectPopup( jQuery.trim(jQuery( "#" + this.id + " #" + data.rslt.obj[0].id + " a:first").text()), provider );
                                                selectNode([{name:"selectedNodeId", value:data.rslt.obj[0].id.replace("node_","")}]);
				    	}
				    });
			});
}

function initPopdown(id){
	
	
	jQuery("#"+ id +" a.popdownToggler").click(function(e) {
		e.stopImmediatePropagation();
        jQuery("#"+ id +" .popdownContent").toggle();
        jQuery("#"+ id +" .popdownToggler").toggleClass("popdown-open");
        return false;
    });

	jQuery("#"+ id +" .popdownContent").mouseup(function() {
        return false
    });
	jQuery(document).mouseup(function(e) {
        if(jQuery(e.target).parent("a.popdownToggler").length==0) {
        	jQuery(".popdownToggler").removeClass("popdown-open");
        	jQuery(".popdownContent").hide();
        }
    });            
}

function initNumberColumns(){
	jQuery(".numberColumn").each(function() {
		jQuery(this.parentNode).addClass("numberColumn");
    });
}


function initSortList(id, up, down){
	jQuery("#" + id + " span").each(function(){
		jQuery(this).click(function(e){
			//Diğer seçilileri kaldır
			jQuery(".selectedRow").removeClass("selectedRow");
			//Bunu seç
			jQuery(this).addClass("selectedRow");
		});
		jQuery(this).dblclick(function(e){
			alert(this);
		});
	});
	//Up butonu
	jQuery("#" + up).click(function(e){
		sortItemUp(jQuery("#" + id + " .selectedRow").attr("id").replace("sorter-item-",""));
	});
	//Down butonu
	jQuery("#" + down).click(function(e){
		alert(this);
	});
}

function popdown( id ){

    content = jQuery("div#popdown"+id);
    toggle = jQuery("a.popdown#toggle"+id);

    //p = toggle.offset();
    //content.css("top", p.top + toggle.outerHeight() - 1 ).css("left",p.left - 4);
    //Popup içinde doğru hesaplamadı yukardaki formul
    content.css("top", 220 ).css("left",180);
    
    toggle.toggleClass("popdown-open");
    content.slideDown(300);
    

    content.mouseup(function() {
        return false
    });
    jQuery(document).mouseup(function(e) {
        if(jQuery(e.target).parent("a.popdown#toggle"+id).length==0) {
            toggle.removeClass("popdown-open");
            content.slideUp(300);
        }
    });
}


function initAcordionTabs(){
	  jQuery(".accordion.hided").next().hide();
	  
	  //toggle the componenet with class msg_body
	  jQuery(".accordion:not(.inited) .accordionLabel, .accordion:not(.inited) .accordionIcon" ).click(function()
	  {
	    var parent = jQuery(this).parent();
            parent.next(".accordioncontent").slideToggle(500);
            var icon = parent.children(".accordionIcon");
            if( icon.hasClass("ui-icon-triangle-1-e")){
                icon.removeClass("ui-icon-triangle-1-e");
                icon.addClass("ui-icon-triangle-1-s");
            } else {
                icon.removeClass("ui-icon-triangle-1-s");
                icon.addClass("ui-icon-triangle-1-e");
            }
	  });

	  jQuery(".accordion").addClass("inited");
}

/**
 * Id si verilen elemana fade out effecti uygular.
 * 
 * @param elemId
 * 		Html eleman id si
 */
function fadeOut( elemId ) {
	var elem = jQuery( document.getElementById(elemId) );
	
	elem.fadeOut( 'fast' );
}


/**
 * Id si verilen elemana fade in effecti uygular.
 * 
 * @param elemId
 * 		Html eleman id si
 */
function fadeIn( elemId ) {
	var elem = jQuery( document.getElementById(elemId) );
	
	elem.fadeIn( 'fast' );
}

            
function enableRibbonBtn( ids ){
     jQuery(ids).each( function(){ 
         jQuery(this).enable();
     });
 }
 function disableRibbonBtn( ids ){
     jQuery(ids).each( function(){ 
         jQuery(this).disable();
     });
 }