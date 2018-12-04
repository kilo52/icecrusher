;This file will be executed next to the application bundle image
;I.e. current directory will contain folder icecrusher with application files
[Setup]
AppId={{com.kilo52.icecrusher}}
AppName=Icecrusher
AppVersion=1.0.0
AppVerName=icecrusher 1.0.0
AppPublisher=kilo52
AppComments=icecrusher
AppCopyright=Copyright (C) 2018
DefaultDirName={localappdata}\icecrusher
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=kilo52
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=icecrusher-1.0.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=icecrusher\icecrusher.ico
UninstallDisplayIcon={app}\icecrusher.ico
UninstallDisplayName=icecrusher
WizardImageStretch=No
WizardSmallImageFile=icecrusher-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "icecrusher\icecrusher.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "icecrusher\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\icecrusher"; Filename: "{app}\icecrusher.exe"; IconFilename: "{app}\icecrusher.ico"; Check: returnTrue()
Name: "{commondesktop}\icecrusher"; Filename: "{app}\icecrusher.exe";  IconFilename: "{app}\icecrusher.ico"; Check: returnFalse()


[Run]
Filename: "{app}\icecrusher.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\icecrusher.exe"; Description: "{cm:LaunchProgram,icecrusher}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\icecrusher.exe"; Parameters: "-install -svcName ""icecrusher"" -svcDesc ""icecrusher"" -mainExe ""icecrusher.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\icecrusher.exe "; Parameters: "-uninstall -svcName icecrusher -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
