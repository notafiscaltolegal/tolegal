/*******************************************************************************
 * (c) Ger Versluis 2000 version 7 17 Februar 2002 * You may use this script on
 * non commercial sites. * For info write to menus@burmees.nl *
 ******************************************************************************/

var AgntUsr = navigator.userAgent.toLowerCase();
var AppVer = navigator.appVersion.toLowerCase();
var DomYes = document.getElementById ? 1 : 0;
var NavYes = AgntUsr.indexOf('mozilla') != -1 && AgntUsr.indexOf('compatible') == -1 ? 1 : 0;
var ExpYes = AgntUsr.indexOf('msie') != -1 ? 1 : 0;
var Opr = AgntUsr.indexOf('opera') != -1 ? 1 : 0;
var DomNav = DomYes && NavYes ? 1 : 0;
var DomExp = DomYes && ExpYes ? 1 : 0;
var Nav4 = NavYes && !DomYes && document.layers ? 1 : 0;
var Exp4 = ExpYes && !DomYes && document.all ? 1 : 0;
var MacCom = (AppVer.indexOf("mac") != -1) ? 1 : 0;
var MacExp4 = (MacCom && AppVer.indexOf("msie 4") != -1) ? 1 : 0;
var MacExp50 = (MacCom && AppVer.indexOf("msie 5.0") != -1) ? 1 : 0;
var Mac4 = (MacCom && (Nav4 || Exp4)) ? 1 : 0;
var PosStrt = (NavYes || ExpYes) && !Opr ? 1 : 0;

var FLoc, ScLoc, DcLoc;
var SWinW, SWinH, FWinW, FWinH;
var SLdAgnWin;
var FColW, SColW, DColW;
var RLvl = 0;
var FrstCreat = 1, Ldd = 0, Crtd = 0, IniFlg, AcrssFrms = 1;
var FrstCntnr = null, CurOvr = null, CloseTmr = null;
var CntrTxt, TxtClose, ImgStr;
var Ztop = 100;
var ShwFlg = 0;
var M_StrtTp = StartTop, M_StrtLft = StartLeft;
var StaticPos = 0;
var LftXtra = DomNav ? LeftPaddng : 0;
var TpXtra = DomNav ? TopPaddng : 0;
var M_Hide = Nav4 ? 'hide' : 'hidden';
var M_Show = Nav4 ? 'show' : 'visible';
var Par = parent.frames[0] && FirstLineFrame != SecLineFrame ? parent : window;
var Doc = Par.document;
var Bod = Doc.body;
var Trigger = NavYes ? Par : Bod;

MenuTextCentered = MenuTextCentered == 1 || MenuTextCentered == 'center' ? 'center' : MenuTextCentered == 0 || MenuTextCentered != 'right' ? 'left' : 'right';
WbMstrAlrts = [ "Item not defined: ", "Item needs height: ", "Item needs width: " ];

if (!MacExp4 && Trigger.onload)
    Dummy = Trigger.onload;
if (MacExp4 || MacExp50)
    Trigger.onload = Go();
else {
    if (DomNav && !Opr)
        Trigger.addEventListener('load', Go, false);
    else
        Trigger.onload = Go;
}

function Dummy() {
    return;
}

function CnclSlct() {
    return false;
}

function verificarMedida(valor){
    if(!isNaN(Number(valor))){
        return valor + "px";
    }
    else {
        return valor;
    }
}

function RePos() {
    FWinW = ExpYes ? FLoc.document.body.clientWidth : FLoc.innerWidth;
    FWinH = ExpYes ? FLoc.document.body.clientHeight : FLoc.innerHeight;
    SWinW = ExpYes ? ScLoc.document.body.clientWidth : ScLoc.innerWidth;
    SWinH = ExpYes ? ScLoc.document.body.clientHeight : ScLoc.innerHeight;
    if (MenuCentered == 'justify' && FirstLineHorizontal) {
        FrstCntnr.style.width = verificarMedida(FWinW);
        ClcJus();
        var P = FrstCntnr.FrstMbr, W = Menu1[5], i;
        for (i = 0; i < NoOffFirstLineMenus; i++) {
            P.style.width = verificarMedida(W);
            P = P.PrvMbr;
        }
    }
    StaticPos = -1;
    if (TargetLoc)
        ClcTrgt();
    if (MenuCentered)
        ClcLft();
    if (MenuVerticalCentered)
        ClcTp();
    PosMenu(FrstCntnr, StartTop, StartLeft);
}

function UnLdd() {
    if (CloseTmr)
        clearTimeout(CloseTmr);
    Ldd = 0;
    Crtd = 0;
    if (HideTop) {
        var St = Nav4 ? FrstCntnr : FrstCntnr.style;
        St.visibility = M_Hide;
    }
    if (!Nav4)
        LdTmr = setInterval("ChckLdd()", 100);
}

function ChckLdd() {
    if (!ExpYes) {
        if (ScLoc.document.body) {
            clearInterval(LdTmr);
            Go();
        }
    } else {
        try {
            if (ScLoc.document.readyState == "complete") {
                if (LdTmr)
                    clearInterval(LdTmr);
                Go();
            }
        } catch (e) {}
    }
}

function NavLdd(e) {
    if (e.target.name == SecLineFrame) {
        routeEvent(e);
        Go();
    }
}

function ReDoWhole() {
    if (SWinW != ScLoc.innerWidth || SWinH != ScLoc.innerHeight || FWinW != FLoc.innerWidth || FWinH != FLoc.innerHeight)
        Doc.location.reload();
}

function Check(WM, n) {
    var i, A, ALoc;
    ALoc = parent.frames[0] ? parent.frames[FirstLineFrame] : self;
    for (i = 0; i < n; i++) {
        A = WM + eval(i + 1);
        if (!ALoc[A]) {
            WbMstrAlrt(0, A);
            return false;
        }
        if (i == 0) {
            if (!ALoc[A][4]) {
                WbMstrAlrt(1, A);
                return false;
            }
            if (!ALoc[A][5]) {
                WbMstrAlrt(2, A);
                return false;
            }
        }
        if (ALoc[A][3])
            if (!Check(A + '_', ALoc[A][3]))
                return false;
    }
    return true;
}

function WbMstrAlrt(No, Xtra) {
    return confirm(WbMstrAlrts[No] + Xtra + '   ');
}

function Go() {
    Dummy();
    if (Ldd || !PosStrt)
        return;
    BeforeStart();
    Crtd = 0;
    Ldd = 1;
    status = 'Building menu';
    if (FrstCreat) {
        if (FirstLineFrame == "" || !parent.frames[FirstLineFrame]) {
            FirstLineFrame = SecLineFrame;
            if (FirstLineFrame == "" || !parent.frames[FirstLineFrame]) {
                FirstLineFrame = SecLineFrame = DocTargetFrame;
                if (FirstLineFrame == "" || !parent.frames[FirstLineFrame])
                    FirstLineFrame = SecLineFrame = DocTargetFrame = '';
            }
        }
        if (SecLineFrame == "" || !parent.frames[SecLineFrame]) {
            SecLineFrame = DocTargetFrame;
            if (SecLineFrame == "" || !parent.frames[SecLineFrame])
                SecLineFrame = DocTargetFrame = FirstLineFrame;
        }
        if (DocTargetFrame == "" || !parent.frames[DocTargetFrame])
            DocTargetFrame = SecLineFrame;
        if (WebMasterCheck) {
            if (!Check('Menu', NoOffFirstLineMenus)) {
                status = 'build aborted';
                return
            }
        }
        FLoc = FirstLineFrame != "" ? parent.frames[FirstLineFrame] : window;
        ScLoc = SecLineFrame != "" ? parent.frames[SecLineFrame] : window;
        DcLoc = DocTargetFrame != "" ? parent.frames[DocTargetFrame] : window;
        if (FLoc == ScLoc)
            AcrssFrms = 0;
        if (AcrssFrms)
            FirstLineHorizontal = MenuFramesVertical ? 0 : 1;
        FWinW = ExpYes ? FLoc.document.body.clientWidth : FLoc.innerWidth;
        FWinH = ExpYes ? FLoc.document.body.clientHeight : FLoc.innerHeight;
        SWinW = ExpYes ? ScLoc.document.body.clientWidth : ScLoc.innerWidth;
        SWinH = ExpYes ? ScLoc.document.body.clientHeight : ScLoc.innerHeight;
    }
    FColW = Nav4 ? FLoc.document : FLoc.document.body;
    SColW = Nav4 ? ScLoc.document : ScLoc.document.body;
    DColW = Nav4 ? DcLoc.document : ScLoc.document.body;
    if (TakeOverBgColor)
        FColW.bgColor = AcrssFrms ? SColW.bgColor : DColW.bgColor;
    if (MenuCentered == 'justify' && FirstLineHorizontal)
        ClcJus();
    if (FrstCreat) {
        FrstCntnr = CreateMenuStructure('Menu', NoOffFirstLineMenus);
        FrstCreat = AcrssFrms ? 0 : 1;
        if (Nav4) {
            Trigger.captureEvents(Event.LOAD);
            Trigger.onload = NavLdd;
        }
    } else
        CreateMenuStructureAgain('Menu', NoOffFirstLineMenus);
    if (TargetLoc)
        ClcTrgt();
    if (MenuCentered)
        ClcLft();
    if (MenuVerticalCentered)
        ClcTp();
    PosMenu(FrstCntnr, StartTop, StartLeft);
    IniFlg = 1;
    Initiate();
    Crtd = 1;
    SLdAgnWin = ExpYes ? ScLoc.document.body : ScLoc;
    SLdAgnWin.onunload = UnLdd;
    Trigger.onresize = Nav4 ? ReDoWhole : RePos;
    AfterBuild();
    if (MenuVerticalCentered == 'static' && !AcrssFrms)
        setInterval('KeepPos()', 250);
    status = 'Menu ready for use';
}

function KeepPos() {
    var TS = ExpYes ? FLoc.document.body.scrollTop : FLoc.pageYOffset;
    if (TS != StaticPos) {
        var FCSt = Nav4 ? FrstCntnr : FrstCntnr.style;
        FCSt.top = FrstCntnr.OrgTop = StartTop + TS;
        StaticPos = TS;
    }
}

function ClcJus() {
    var a = BorderBtwnElmnts ? 1 : 2, b = BorderBtwnElmnts ? BorderWidth : 0;
    var Sz = Math.round(((FWinW - a * BorderWidth) / NoOffFirstLineMenus) - b), i, j;
    for (i = 1; i < NoOffFirstLineMenus + 1; i++) {
        j = eval('Menu' + i);
        j[5] = Sz;
    }
    StartLeft = 0;
}

function ClcTrgt() {
    var TLoc = Nav4 ? FLoc.document.layers[TargetLoc] : DomYes ? FLoc.document.getElementById(TargetLoc) : FLoc.document.all[TargetLoc];
    StartTop = M_StrtTp;
    StartLeft = M_StrtLft;
    if (DomYes) {
        while (TLoc) {
            StartTop += TLoc.offsetTop;
            StartLeft += TLoc.offsetLeft;
            TLoc = TLoc.offsetParent;
        }
    } else {
        StartTop += Nav4 ? TLoc.pageY : TLoc.offsetTop;
        StartLeft += Nav4 ? TLoc.pageX : TLoc.offsetLeft;
    }
}

function ClcLft() {
    if (MenuCentered != 'left' && MenuCentered != 'justify') {
        var Sz = FWinW - (!Nav4 ? parseInt(FrstCntnr.style.width) : FrstCntnr.clip.width);
        StartLeft = M_StrtLft;
        StartLeft += MenuCentered == 'right' ? Sz : Sz / 2;
    }
}

function ClcTp() {
    if (MenuVerticalCentered != 'top' && MenuVerticalCentered != 'static') {
        var Sz = FWinH - (!Nav4 ? parseInt(FrstCntnr.style.height) : FrstCntnr.clip.height);
        StartTop = M_StrtTp;
        StartTop += MenuVerticalCentered == 'bottom' ? Sz : Sz / 2;
    }
}

function PosMenu(Ct, Tp, Lt) {
    var Ti, Li, Hi;
    var Mb = Ct.FrstMbr;
    var CStl = !Nav4 ? Ct.style : Ct;
    var MStl = !Nav4 ? Mb.style : Mb;
    var PadL = Mb.value.indexOf('<') == -1 ? LftXtra : 0;
    var PadT = Mb.value.indexOf('<') == -1 ? TpXtra : 0;
    var MWt = !Nav4 ? parseInt(MStl.width) + PadL : MStl.clip.width;
    var MHt = !Nav4 ? parseInt(MStl.height) + PadT : MStl.clip.height;
    var CWt = !Nav4 ? parseInt(CStl.width) : CStl.clip.width;
    var CHt = !Nav4 ? parseInt(CStl.height) : CStl.clip.height;
    var CCw, CCh;
    var STp, SLt;
    RLvl++;
    if (RLvl == 1 && AcrssFrms)
        !MenuFramesVertical ? Tp = BottomUp ? 0 : FWinH - CHt + (Nav4 ? 4 : 0) : Lt = RightToLeft ? 0 : FWinW - CWt + (Nav4 ? 4 : 0);
    if (RLvl == 2 && AcrssFrms)
        !MenuFramesVertical ? Tp = BottomUp ? SWinH - CHt + (Nav4 ? 4 : 0) : 0 : Lt = RightToLeft ? SWinW - CWt : 0;
    if (RLvl == 2 && AcrssFrms) {
        Tp += VerCorrect;
        Lt += HorCorrect;
    }
    CStl.top = verificarMedida(RLvl == 1 ? Tp : 0);
    Ct.OrgTop = Tp;
    CStl.left = verificarMedida(RLvl == 1 ? Lt : 0);
    Ct.OrgLeft = Lt;
    if (RLvl == 1 && FirstLineHorizontal) {
        Hi = 1;
        Li = CWt - MWt - 2 * BorderWidth;
        Ti = 0;
    } else {
        Hi = Li = 0;
        Ti = CHt - MHt - 2 * BorderWidth;
    }
    while (Mb != null) {
        MStl.left = verificarMedida(Li + BorderWidth);
        MStl.top = verificarMedida(Ti + BorderWidth);
        if (Nav4)
            Mb.CLyr.moveTo(Li + BorderWidth, Ti + BorderWidth);
        if (Mb.CCn) {
            if (RightToLeft)
                CCw = Nav4 ? Mb.CCn.clip.width : parseInt(Mb.CCn.style.width);
            if (BottomUp)
                CCh = Nav4 ? Mb.CCn.clip.height : parseInt(Mb.CCn.style.height);
            if (Hi) {
                STp = BottomUp ? Ti - CCh : Ti + MHt + 2 * BorderWidth;
                SLt = RightToLeft ? Li + MWt - CCw : Li;
            } else {
                SLt = RightToLeft ? Li - CCw + ChildOverlap * MWt + BorderWidth : Li + (1 - ChildOverlap) * MWt + BorderWidth;
                STp = RLvl == 1 && AcrssFrms ? BottomUp ? Ti - CCh + MHt : Ti : BottomUp ? Ti - CCh + (1 - ChildVerticalOverlap) * MHt + 2 * BorderWidth : Ti + ChildVerticalOverlap * MHt;
            }
            PosMenu(Mb.CCn, STp, SLt);
        }
        Mb = Mb.PrvMbr;
        if (Mb) {
            MStl = !Nav4 ? Mb.style : Mb;
            PadL = Mb.value.indexOf('<') == -1 ? LftXtra : 0;
            PadT = Mb.value.indexOf('<') == -1 ? TpXtra : 0;
            MWt = !Nav4 ? parseInt(MStl.width) + PadL : MStl.clip.width;
            MHt = !Nav4 ? parseInt(MStl.height) + PadT : MStl.clip.height;
            Hi ? Li -= BorderBtwnElmnts ? (MWt + BorderWidth) : (MWt) : Ti -= BorderBtwnElmnts ? (MHt + BorderWidth) : MHt;
        }
    }
    RLvl--;
}

function Initiate() {
    if (IniFlg) {
        Init(FrstCntnr);
        IniFlg = 0;
        if (ShwFlg)
            AfterCloseAll();
        ShwFlg = 0;
    }
}

function Init(CPt) {
    var Mb = CPt.FrstMbr;
    var MCSt = Nav4 ? CPt : CPt.style;
    RLvl++;
    MCSt.visibility = RLvl == 1 ? M_Show : M_Hide;
    while (Mb != null) {
        if (Mb.Hilite) {
            Mb.Hilite = 0;
            if (KeepHilite)
                LowItem(Mb);
        }
        if (Mb.CCn)
            Init(Mb.CCn);
        Mb = Mb.PrvMbr;
    }
    RLvl--;
}

function ClrAllChlds(Pt) {
    var PSt;
    while (Pt) {
        if (Pt.Hilite) {
            Pt.Hilite = 0;
            if (KeepHilite)
                LowItem(Pt);
            if (Pt.CCn) {
                PSt = Nav4 ? Pt.CCn : Pt.CCn.style;
                PSt.visibility = M_Hide;
                ClrAllChlds(Pt.CCn.FrstMbr);
            }
            break;
        }
        Pt = Pt.PrvMbr;
    }
}

function GoTo() {
    var HP = Nav4 ? this.LLyr : this;
    if (HP.Arr[1]) {
        status = '';
        LowItem(HP);
        IniFlg = 1;
        Initiate();
        HP.Arr[1].indexOf('javascript:') != -1 ? eval(HP.Arr[1]) : DcLoc.location.href = HP.Arr[1];
    }
}

function HiliteItem(P) {
    if (Nav4) {
        if (P.ro)
            P.document.images[P.rid].src = P.ri2;
        else {
            if (P.Arr[7])
                P.bgColor = P.Arr[7];
            if (P.value.indexOf('<img') == -1) {
                P.document.write(P.Ovalue);
                P.document.close();
            }
        }
    } else {
        if (P.ro) {
            var Lc = P.Lvl == 1 ? FLoc : ScLoc;
            Lc.document.images[P.rid].src = P.ri2;
        } else {
            if (P.Arr[7])
                P.style.backgroundColor = P.Arr[7];
            if (P.Arr[9])
                P.style.color = P.Arr[9];
        }
    }
    P.Hilite = 1;
}

function LowItem(P) {
    if (P.ro) {
        if (Nav4)
            P.document.images[P.rid].src = P.ri1;
        else {
            var Lc = P.Lvl == 1 ? FLoc : ScLoc;
            Lc.document.images[P.rid].src = P.ri1;
        }
    } else {
        if (Nav4) {
            if (P.Arr[6])
                P.bgColor = P.Arr[6];
            if (P.value.indexOf('<img') == -1) {
                P.document.write(P.value);
                P.document.close();
            }
        } else {
            if (P.Arr[6])
                P.style.backgroundColor = P.Arr[6];
            if (P.Arr[8])
                P.style.color = P.Arr[8];
        }
    }
}

function OpenMenu() {
    if (!Ldd || !Crtd)
        return;
    var TS = ExpYes ? ScLoc.document.body.scrollTop : ScLoc.pageYOffset;
    var LS = ExpYes ? ScLoc.document.body.scrollLeft : ScLoc.pageXOffset;
    var CCnt = Nav4 ? this.LLyr.CCn : this.CCn;
    var THt = Nav4 ? this.clip.height : parseInt(this.style.height);
    var TWt = Nav4 ? this.clip.width : parseInt(this.style.width);
    var TLt = AcrssFrms && this.Lvl == 1 && !FirstLineHorizontal ? 0 : Nav4 ? this.Ctnr.left : parseInt(this.Ctnr.style.left);
    var TTp = AcrssFrms && this.Lvl == 1 && FirstLineHorizontal ? 0 : Nav4 ? this.Ctnr.top : parseInt(this.Ctnr.style.top);
    var HP = Nav4 ? this.LLyr : this;
    CurOvr = this;
    IniFlg = 0;
    ClrAllChlds(this.Ctnr.FrstMbr);
    HiliteItem(HP);
    if (CCnt != null) {
        if (!ShwFlg) {
            ShwFlg = 1;
            BeforeFirstOpen();
        }
        var CCW = Nav4 ? this.LLyr.CCn.clip.width : parseInt(this.CCn.style.width);
        var CCH = Nav4 ? this.LLyr.CCn.clip.height : parseInt(this.CCn.style.height);
        var CCSt = Nav4 ? this.LLyr.CCn : this.CCn.style;
        var SLt = AcrssFrms && this.Lvl == 1 ? CCnt.OrgLeft + TLt + LS : CCnt.OrgLeft + TLt;
        var STp = AcrssFrms && this.Lvl == 1 ? CCnt.OrgTop + TTp + TS : CCnt.OrgTop + TTp;
        if (MenuWrap) {
            if (RightToLeft) {
                if (SLt < LS)
                    SLt = this.Lvl == 1 ? LS : SLt + (CCW + (1 - 2 * ChildOverlap) * TWt);
                if (SLt + CCW > SWinW + LS)
                    SLt = SWinW + LS - CCW;
            } else {
                if (SLt + CCW > SWinW + LS)
                    SLt = this.Lvl == 1 ? SWinW + LS - CCW : SLt - (CCW + (1 - 2 * ChildOverlap) * TWt);
                if (SLt < LS)
                    SLt = LS;
            }
            if (BottomUp) {
                if (STp < TS)
                    STp = this.Lvl == 1 ? TS : STp + (CCH - (1 - 2 * ChildVerticalOverlap) * THt);
                if (STp + CCH > SWinH + TS)
                    STp = SWinH + TS - CCH + (Nav4 ? 4 : 0);
            } else {
                if (STp + CCH > TS + SWinH)
                    STp = this.Lvl == 1 ? STp = TS + SWinH - CCH : STp - CCH + (1 - 2 * ChildVerticalOverlap) * THt;
                if (STp < TS)
                    STp = TS;
            }
        }
        CCSt.top = verificarMedida(STp);
        CCSt.left = verificarMedida(SLt);
        CCSt.visibility = M_Show;
    }
    status = HP.Arr[16];
}

function OpenMenuClick() {
    if (!Ldd || !Crtd)
        return;
    var HP = Nav4 ? this.LLyr : this;
    CurOvr = this;
    IniFlg = 0;
    ClrAllChlds(this.Ctnr.FrstMbr);
    HiliteItem(HP);
    status = HP.Arr[16];
}

function CloseMenu() {
    if (!Ldd || !Crtd)
        return;
    if (!KeepHilite) {
        var HP = Nav4 ? this.LLyr : this;
        LowItem(HP);
    }
    status = '';
    if (this == CurOvr) {
        IniFlg = 1;
        if (CloseTmr)
            clearTimeout(CloseTmr);
        CloseTmr = setTimeout('Initiate(CurOvr)', DissapearDelay);
    }
}

function CntnrSetUp(W, H, WMu) {
    var x = eval(WMu + '[10]') != "" ? eval(WMu + '[10]') : BorderColor;
    this.FrstMbr = null;
    this.OrgLeft = this.OrgTop = 0;
    if (Nav4) {
        if (x)
            this.bgColor = x;
        this.visibility = 'hide';
        this.resizeTo(W, H);
    } else {
        if (x)
            this.style.backgroundColor = x;
        this.style.display = "block";
        this.style.width = verificarMedida(W);
        this.style.height = verificarMedida(H);
        this.style.zIndex = RLvl + Ztop;
    }
}

function MbrSetUp(MbC, PrMmbr, WMu, Wd, Ht) {
    var Lctn = RLvl == 1 ? FLoc : ScLoc;
    var Tfld = this.Arr[0];
    var t, T, L, W, H, S;
    var a;
    this.PrvMbr = PrMmbr;
    this.Lvl = RLvl;
    this.Ctnr = MbC;
    this.CCn = null;
    this.Hilite = 0;
    this.style.overflow = 'hidden';
    this.style.cursor = ExpYes && (this.Arr[1] || (RLvl == 1 && UnfoldsOnClick)) ? 'hand' : 'default';
    this.ro = 0;
    if (Tfld.indexOf('rollover') != -1) {
        this.ro = 1;
        this.ri1 = Tfld.substring(Tfld.indexOf(':') + 1, Tfld.lastIndexOf(':'));
        this.ri2 = Tfld.substring(Tfld.lastIndexOf(':') + 1, Tfld.length);
        this.rid = WMu + 'i';
        Tfld = "<img src='" + this.ri1 + "' name='" + this.rid + "'>";
    }
    this.value = Tfld;
    this.style.color = this.Arr[8];
    this.style.fontFamily = this.Arr[11];
    this.style.fontSize = this.Arr[12] + 'pt';
    this.style.fontWeight = this.Arr[13] ? 'bold' : 'normal';
    this.style.fontStyle = this.Arr[14] ? 'italic' : 'normal';
    if (this.Arr[6])
        this.style.backgroundColor = this.Arr[6];
    this.style.textAlign = this.Arr[15];
    if (this.Arr[2])
        this.style.backgroundImage = "url(\"" + this.Arr[2] + "\")";
    if (Tfld.indexOf('<') == -1) {
        this.style.width = verificarMedida(Wd - LftXtra);
        this.style.height = verificarMedida(Ht - TpXtra);
        this.style.paddingLeft = verificarMedida(LeftPaddng);
        this.style.paddingTop = verificarMedida(TopPaddng);
    } else {
        this.style.width = verificarMedida(Wd);
        this.style.height = verificarMedida(Ht);
    }
    if (Tfld.indexOf('<') == -1 && DomYes) {
        t = Lctn.document.createTextNode(Tfld);
        this.appendChild(t);
    } else
        this.innerHTML = Tfld;
    if (this.Arr[3] && ShowArrow && !this.ro) {
        a = RLvl == 1 && FirstLineHorizontal ? BottomUp ? 9 : 3 : RightToLeft ? 6 : 0;
        S = Arrws[a];
        W = Arrws[a + 1];
        H = Arrws[a + 2];
        T = RLvl == 1 && FirstLineHorizontal ? BottomUp ? 2 : Ht - H - 2 : (Ht - H) / 2;
        L = RightToLeft ? 2 : Wd - W - 2;
        if (DomYes) {
            t = Lctn.document.createElement('img');
            this.appendChild(t);
            t.style.position = 'absolute';
            t.src = S;
            t.style.width = verificarMedida(W);
            t.style.height = verificarMedida(H);
            t.style.top = verificarMedida(T);
            t.style.left = verificarMedida(L);
        } else {
            Tfld += "<div style='position:absolute; top:" + T + "; left:" + L + "; width:" + W + "; height:" + H + ";visibility:inherit'><img src='" + S + "'></div>";
            this.innerHTML = Tfld;
        }
    }
    if (ExpYes) {
        this.onselectstart = CnclSlct;
        this.onmouseover = RLvl == 1 && UnfoldsOnClick ? OpenMenuClick : OpenMenu;
        this.onmouseout = CloseMenu;
        this.onclick = RLvl == 1 && UnfoldsOnClick && this.Arr[3] ? OpenMenu : GoTo;
    } else {
        RLvl == 1 && UnfoldsOnClick ? this.addEventListener('mouseover', OpenMenuClick, false) : this.addEventListener('mouseover', OpenMenu, false);
        this.addEventListener('mouseout', CloseMenu, false);
        RLvl == 1 && UnfoldsOnClick && this.Arr[3] ? this.addEventListener('click', OpenMenu, false) : this.addEventListener('click', GoTo, false);
    }
}

function NavMbrSetUp(MbC, PrMmbr, WMu, Wd, Ht) {
    var a;
    this.value = this.Arr[0];
    this.ro = 0;
    if (this.value.indexOf('rollover') != -1) {
        this.ro = 1;
        this.ri1 = this.value.substring(this.value.indexOf(':') + 1, this.value.lastIndexOf(':'));
        this.ri2 = this.value.substring(this.value.lastIndexOf(':') + 1, this.value.length);
        this.rid = WMu + 'i';
        this.value = "<img src='" + this.ri1 + "' name='" + this.rid + "'>";
    }
    CntrTxt = this.Arr[15] != 'left' ? "<div align='" + this.Arr[15] + "'>" : "";
    TxtClose = "</font>" + this.Arr[15] != 'left' ? "</div>" : "";
    if (LeftPaddng && this.value.indexOf('<') == -1 && this.Arr[15] == 'left')
        this.value = '&nbsp;' + this.value;
    if (this.Arr[13])
        this.value = this.value.bold();
    if (this.Arr[14])
        this.value = this.value.italics();
    this.Ovalue = this.value;
    this.value = this.value.fontcolor(this.Arr[8]);
    this.Ovalue = this.Ovalue.fontcolor(this.Arr[9]);
    this.value = CntrTxt + "<font face='" + this.Arr[11] + "' point-size='" + this.Arr[12] + "'>" + this.value + TxtClose;
    this.Ovalue = CntrTxt + "<font face='" + this.Arr[11] + "' point-size='" + this.Arr[12] + "'>" + this.Ovalue + TxtClose;
    this.CCn = null;
    this.PrvMbr = PrMmbr;
    this.Hilite = 0;
    this.visibility = 'inherit';
    if (this.Arr[6])
        this.bgColor = this.Arr[6];
    this.resizeTo(Wd, Ht);
    if (!AcrssFrms && this.Arr[2])
        this.background.src = this.Arr[2];
    this.document.write(this.value);
    this.document.close();
    this.CLyr = new Layer(Wd, MbC);
    this.CLyr.Lvl = RLvl;

    this.CLyr.visibility = 'inherit';
    this.CLyr.onmouseover = RLvl == 1 && UnfoldsOnClick ? OpenMenuClick : OpenMenu;
    this.CLyr.onmouseout = CloseMenu;
    this.CLyr.captureEvents(Event.MOUSEUP);
    this.CLyr.onmouseup = RLvl == 1 && UnfoldsOnClick && this.Arr[3] ? OpenMenu : GoTo;
    this.CLyr.LLyr = this;
    this.CLyr.resizeTo(Wd, Ht);
    this.CLyr.Ctnr = MbC;
    if (this.Arr[3] && ShowArrow && !this.ro) {
        a = RLvl == 1 && FirstLineHorizontal ? BottomUp ? 9 : 3 : RightToLeft ? 6 : 0;
        this.CLyr.ILyr = new Layer(Arrws[a + 1], this.CLyr);
        this.CLyr.ILyr.visibility = 'inherit';
        this.CLyr.ILyr.top = verificarMedida(RLvl == 1 && FirstLineHorizontal ? BottomUp ? 2 : Ht - Arrws[a + 2] - 2 : (Ht - Arrws[a + 2]) / 2);
        this.CLyr.ILyr.left = verificarMedida(RightToLeft ? 2 : Wd - Arrws[a + 1] - 2);
        this.CLyr.ILyr.width = verificarMedida(Arrws[a + 1]);
        this.CLyr.ILyr.height = verificarMedida(Arrws[a + 2]);
        ImgStr = "<img src='" + Arrws[a] + "' width='" + Arrws[a + 1] + "' height='" + Arrws[a + 2] + "'>";
        this.CLyr.ILyr.document.write(ImgStr);
        this.CLyr.ILyr.document.close();
    }
}

function CreateMenuStructure(MNm, No) {
    RLvl++;
    var i, NOs, Mbr, W = 0, H = 0;
    var PMb = null;
    var WMnu = MNm + '1';
    var MWd = eval(WMnu + '[5]');
    var MHt = eval(WMnu + '[4]');
    var Lctn = RLvl == 1 ? FLoc : ScLoc;
    if (RLvl == 1 && FirstLineHorizontal) {
        for (i = 1; i < No + 1; i++) {
            WMnu = MNm + eval(i);
            W = eval(WMnu + '[5]') ? W + eval(WMnu + '[5]') : W + MWd;
        }
        W = BorderBtwnElmnts ? W + (No + 1) * BorderWidth : W + 2 * BorderWidth;
        H = MHt + 2 * BorderWidth;
    } else {
        for (i = 1; i < No + 1; i++) {
            WMnu = MNm + eval(i);
            H = eval(WMnu + '[4]') ? H + eval(WMnu + '[4]') : H + MHt;
        }
        H = BorderBtwnElmnts ? H + (No + 1) * BorderWidth : H + 2 * BorderWidth;
        W = MWd + 2 * BorderWidth;
    }
    if (DomYes) {
        var MbC = Lctn.document.createElement("div");
        MbC.style.position = 'absolute';
        MbC.style.visibility = 'hidden';
        Lctn.document.body.appendChild(MbC);
    } else {
        if (Nav4)
            var MbC = new Layer(W, Lctn);
        else {
            WMnu += 'c';
            Lctn.document.body.insertAdjacentHTML("AfterBegin", "<div id='" + WMnu + "' style='visibility:hidden; position:absolute;'><\/div>");
            var MbC = Lctn.document.all[WMnu];
        }
    }
    MbC.SetUp = CntnrSetUp;
    MbC.SetUp(W, H, MNm + '1');
    if (Exp4) {
        MbC.InnerString = '';
        for (i = 1; i < No + 1; i++) {
            WMnu = MNm + eval(i);
            MbC.InnerString += "<div id='" + WMnu + "' style='position:absolute;'><\/div>";
        }
        MbC.innerHTML = MbC.InnerString;
    }
    for (i = 1; i < No + 1; i++) {
        WMnu = MNm + eval(i);
        NOs = eval(WMnu + '[3]');
        W = RLvl == 1 && FirstLineHorizontal ? eval(WMnu + '[5]') ? eval(WMnu + '[5]') : MWd : MWd;
        H = RLvl == 1 && FirstLineHorizontal ? MHt : eval(WMnu + '[4]') ? eval(WMnu + '[4]') : MHt;
        if (DomYes) {
            Mbr = Lctn.document.createElement("div");
            Mbr.style.position = 'absolute';
            Mbr.style.visibility = 'inherit';
            MbC.appendChild(Mbr);
        } else
            Mbr = Nav4 ? new Layer(W, MbC) : Lctn.document.all[WMnu];
        Mbr.Arr = eval(WMnu);
        if (Mbr.Arr[6] == "")
            Mbr.Arr[6] = LowBgColor;
        if (Mbr.Arr[7] == "")
            Mbr.Arr[7] = HighBgColor;
        if (Mbr.Arr[8] == "")
            Mbr.Arr[8] = FontLowColor;
        if (Mbr.Arr[9] == "")
            Mbr.Arr[9] = FontHighColor;
        if (Mbr.Arr[11] == "")
            Mbr.Arr[11] = FontFamily;
        if (Mbr.Arr[12] == -1)
            Mbr.Arr[12] = FontSize;
        if (Mac4)
            Mbr.Arr[12] = Math.round(4 * Mbr.Arr[12] / 3);
        if (Mbr.Arr[13] == -1)
            Mbr.Arr[13] = FontBold;
        if (Mbr.Arr[14] == -1)
            Mbr.Arr[14] = FontItalic;
        if (Mbr.Arr[15] == "")
            Mbr.Arr[15] = MenuTextCentered;
        if (Mbr.Arr[16] == "")
            Mbr.Arr[16] = Mbr.Arr[1];
        Mbr.SetUp = Nav4 ? NavMbrSetUp : MbrSetUp;
        Mbr.SetUp(MbC, PMb, WMnu, W, H);
        if (NOs)
            Mbr.CCn = CreateMenuStructure(WMnu + '_', NOs);
        PMb = Mbr;
    }
    MbC.FrstMbr = Mbr;
    RLvl--;
    return (MbC);
}

function CreateMenuStructureAgain(MNm, No) {
    var i, WMnu, NOs, PMb, Mbr = FrstCntnr.FrstMbr;
    RLvl++;
    for (i = No; i > 0; i--) {
        WMnu = MNm + eval(i);
        NOs = eval(WMnu + '[3]');
        PMb = Mbr;
        if (NOs)
            Mbr.CCn = CreateMenuStructure(WMnu + '_', NOs);
        Mbr = Mbr.PrvMbr;
    }
    RLvl--;
}