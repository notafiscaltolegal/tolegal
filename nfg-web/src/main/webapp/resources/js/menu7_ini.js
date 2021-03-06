//* @version $Revision: 1855 $,$Date: 2008-02-18 11:44:40 -0300 (Seg, 18 Fev 2008) $
//(c) Ger Versluis 2000 version 6.3

//var NoOffFirstLineMenus=8;			// Number of first level items
var LowBgColor='#ECF4FA';			// Background color when mouse is not over
var LowSubBgColor='#ECF4FA';			// Background color when mouse is not over on subs
var HighBgColor='#E3F1FA';			// Background color when mouse is over
var HighSubBgColor='#4679AA';			// Background color when mouse is over on subs
var FontLowColor='#333333';			// Font color when mouse is not over
var FontSubLowColor='black';			// Font color subs when mouse is not over
var FontHighColor='#2779B7';			// Font color when mouse is over
var FontSubHighColor='#2779B7';			// Font color subs when mouse is over
var BorderColor='#B3D3EB';			// Border color
var BorderSubColor='#B3D3EB';			// Border color for subs
var BorderWidth=1;				// Border width
var BorderBtwnElmnts=1;			// Border between elements 1 or 0
var FontFamily="Arial,Tahoma,Helvetica,Verdana";	// Font family menu items
var FontSize=8;				// Font size menu items
var FontBold=1;				// Bold menu items 1 or 0
var FontItalic=0;				// Italic menu items 1 or 0
var MenuTextCentered='left';			// Item text position 'left', 'center' or 'right'
var MenuCentered='left';			// Menu horizontal position 'left', 'center' or 'right'
var MenuVerticalCentered='top';		// Menu vertical position 'top', 'middle' or 'bottom'
var ChildOverlap=.2;				// horizontal overlap child/ parent
var ChildVerticalOverlap=.2;			// vertical overlap child/ parent
var StartTop=200;				// Menu offset x coordinate
var StartLeft=8;				// Menu offset y coordinate
var VerCorrect=0;				// Multiple frames y correction
var HorCorrect=0;				// Multiple frames x correction
var LeftPaddng=3;				// Left padding
var TopPaddng=1;				// Top padding
var FirstLineHorizontal=0;			// First level items layout horizontal 1 or 0
var MenuFramesVertical=0;			// Frames in cols or rows 1 or 0
var DissapearDelay=1000;			// delay before menu folds in
var TakeOverBgColor=1;			// Menu frame takes over background color subitem frame
var FirstLineFrame='menu';			// Frame where first level appears
var SecLineFrame='principal';			// Frame where sub levels appear
var DocTargetFrame='space';			// Frame where target documents appear
var TargetLoc='';				// DIV id for relative positioning (refer to config.htm for info)
var HideTop=0;				// Hide first level when loading new document 1 or 0
var MenuWrap=0;				// enables/ disables menu wrap 1 or 0
var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
var BottomUp=0;
var UnfoldsOnClick=0;			// Level 1 unfolds onclick/ onmouseover
var WebMasterCheck=0;			// menu tree checking on or off 1 or 0
var ShowArrow=1;
var KeepHilite=1;
//var Arrws=['/js/tri.gif',4,8,'/js/tridown.gif',8,4,'/js/trileft.gif',5,10,'/js/triup.gif',10,5];
var Arrws=['/portalsefaz/imagens/tri.gif',4,8,'/portalsefaz/imagens/tridown.gif',8,4,'/portalsefaz/imagens/trileft.gif',5,10,'/portalsefaz/imagens/triup.gif',10,5];
var TamLinha=260;
var TamLetra=10.1;
var AltMenu=16;
var AltItem=20;

function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){
    visivel (false);
}

function AfterCloseAll(){
    visivel (true);
}

var sitMenuAtivo = false;

function visivel(iver){
    for (var f=0; f<ScLoc.document.forms.length; f++)
        if(ScLoc.tratarForm) {
            H_El= ScLoc.document[f];
            H_Vis=(Nav4)?H_El:H_El.style;
            if (iver)
                H_Vis.visibility=M_Show;
            else
                H_Vis.visibility=M_Hide;
        } else {
            if (ScLoc && ScLoc.document[f] && ScLoc.document[f].elements) {
                for (var e=0; e<ScLoc.document[f].elements.length; e++) {
                    H_El= ScLoc.document[f].elements[e];
                    tipo = H_El.type;
                    if ( (tipo == "select-one" ) || (tipo == "select-multiple") ) {
                        H_Vis=(Nav4)?H_El:H_El.style;
                        if (iver)
                            H_Vis.visibility=M_Show;
                        else
                            H_Vis.visibility=M_Hide;
                    }
                }
            }
        }
    sitMenuAtivo = ! iver;
}