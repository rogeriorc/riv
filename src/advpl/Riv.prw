#include "TOTVS.CH"

Class Riv From CloudBridgeApp
	Data StartTime

	Method New() Constructor
	Method OnStart()
	Method OnLoadFinished(url)
EndClass

Method New() Class Riv
	SELF:StartTime:= Seconds()
Return

Method OnStart() Class Riv
	SELF:WebView:navigate(SELF:RootPath + "index.html")
Return

Method OnLoadFinished(url) Class Riv
Return


User Function Riv()
Return
