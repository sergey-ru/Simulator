/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.data;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergei
 */
public class SystemEvent {
    
    
    // <editor-fold defaultstate="collapsed" desc="Data properties">
    
    
    private Date _TimeOfEvent;
    private int _index ;
    private String _ProcessName;
    private int _PID;
    private OperationType _Operation;
    private String _Path;
    private ResultType _Result;
    private String _Detail;

    public Date getTimeOfEvent() {
        return _TimeOfEvent;
    }
    
    public EventType getTypeOfEvent() {
        return this._Operation.TypeOfEvent();
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }
    
    public String getProcessName() {
        return _ProcessName;
    }

    public void setProcessName(String _ProcessName) {
        this._ProcessName = _ProcessName;
    }

    public int getPID() {
        return _PID;
    }

    public void setPID(int _PID) {
        this._PID = _PID;
    }

    public OperationType getOperation() {
        return _Operation;
    }

    public void setOperation(OperationType _Operation) {
        this._Operation = _Operation;
    }

    public String getPath() {
        return _Path;
    }

    public void setPath(String _Path) {
        this._Path = _Path;
    }

    public ResultType getResult() {
        return _Result;
    }

    public void setResult(ResultType _Result) {
        this._Result = _Result;
    }

    public String getDetail() {
        return _Detail;
    }

    public void setDetail(String _Detail) {
        this._Detail = _Detail;
    }
    
    // </editor-fold>
    
    public SystemEvent(String p_CSVRow){
        
        String [] data = p_CSVRow.split("\\s*,\\s*");
        
        this._TimeOfEvent = new Date();
        
        if(data.length == 7){
            //Process Name
            if(data[1] != null && !"".equals(data[1]))
            {
                this._ProcessName = data[1];
            }
            //PID
            if(data[2] != null && !"".equals(data[2]))
            {
                Integer PID = Integer.getInteger(data[2]);
                if(PID!=null)
                {
                    this._PID = PID.intValue();
                }
                else
                {
                    Logger.getLogger(SystemEvent.class.getName()).log(Level.SEVERE, null, "Unable to parse PID to integer in constructor.");
                }
            }
            //Operation
            if(data[3] != null && !"".equals(data[3]))
            {
                boolean opFound =false;
                for (OperationType op : OperationType.values()) {
                    if(op.textValue().equals(data[3]))
                    {
                        this._Operation = op;
                        opFound=true;
                        break;
                    }
                }
                if(!opFound)
                {
                    Logger.getLogger(SystemEvent.class.getName()).log(Level.SEVERE, null, "Unable to parse the operation in constructor.");
                }
                
            }
            //Path
            if(data[4] != null && !"".equals(data[4]))
            {
                this._Path = data[4];
            }
            //Result
            if(data[5] != null && !"".equals(data[5]))
            {
                boolean resFound =false;
                for (ResultType res : ResultType.values()) {
                    if(res.textValue().equals(data[5]))
                    {
                        this._Result = res;
                        resFound=true;
                        break;
                    }
                }
                if(!resFound)
                {
                    Logger.getLogger(SystemEvent.class.getName()).log(Level.SEVERE, null, "Unable to parse the result in constructor.");
                }
            }
            //Detail
            if(data[6] != null && !"".equals(data[6]))
            {
                this._Detail = data[6];
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Class enums">
    
    public enum EventType {
        Registry, FileSystem, Network, ProcessAndThread,Unknown
    }
    
    public enum OperationType {
        CancelRemoveDevice                                  ("CancelRemoveDevice",EventType.Unknown),
        CancelStopDevice                                    ("CancelStopDevice",EventType.Unknown),
        CloseFile                                           ("CloseFile",EventType.FileSystem),
        CreateFile                                          ("CreateFile",EventType.FileSystem),
        CreateFileMapping                                   ("CreateFileMapping",EventType.FileSystem),
        CreateMailSlot                                      ("CreateMailSlot",EventType.Unknown),
        CreatePipe                                          ("CreatePipe",EventType.Unknown),
        DebugOutputProfiling                                ("Debug Output Profiling",EventType.Unknown),
        DeviceChange                                        ("DeviceChange",EventType.Unknown),
        DeviceIoControl                                     ("DeviceIoControl	",EventType.Unknown),
        DeviceUsageNotification                             ("DeviceUsageNotification",EventType.Unknown),
        Eject                                               ("Eject",EventType.Unknown),
        FileStreamInformation                               ("FileStreamInformation",EventType.FileSystem),
        FileSystemControl                                   ("FileSystemControl",EventType.FileSystem),
        FilterResourceRequirements                          ("FilterResourceRequirements",EventType.Unknown),
        FlushBuffersFile                                    ("FlushBuffersFile",EventType.FileSystem),
        InternalDeviceIoControl                             ("InternalDeviceIoControl",EventType.Unknown),
        LoadImage                                           ("Load Image",EventType.Unknown),
        LockFile                                            ("LockFile",EventType.FileSystem),
        NotifyChangeDirectory                               ("NotifyChangeDirectory",EventType.FileSystem),
        Power                                               ("Power",EventType.Unknown),
        ProcessCreate                                       ("Process Create",EventType.ProcessAndThread),
        ProcessExit                                         ("Process Exit",EventType.ProcessAndThread),
        ProcessProfiling                                    ("Process Profiling",EventType.ProcessAndThread),
        ProcessStart                                        ("Process Start",EventType.ProcessAndThread),
        ProcessStatistics                                   ("Process Statistics",EventType.ProcessAndThread),
        QueryAllInformationFile                             ("QueryAllInformationFile",EventType.FileSystem),
        QueryAttributeCacheInformation                      ("QueryAttributeCacheInformation",EventType.Unknown),
        QueryAttributeInformationVolume                     ("QueryAttributeInformationVolume",EventType.Unknown),
        QueryAttributeTag                                   ("QueryAttributeTag",EventType.Unknown),
        QueryAttributeTagFile                               ("QueryAttributeTagFile",EventType.FileSystem),
        QueryBasicInformationFile                           ("QueryBasicInformationFile",EventType.FileSystem),
        QueryBusInformation                                 ("QueryBusInformation",EventType.Unknown),
        QueryCapabilities                                   ("QueryCapabilities",EventType.Unknown),
        QueryCompressionInformationFile                     ("QueryCompressionInformationFile",EventType.FileSystem),
        QueryDeviceInformationVolume                        ("QueryDeviceInformationVolume",EventType.Unknown),
        QueryDeviceRelations                                ("QueryDeviceRelations",EventType.Unknown),
        QueryDeviceText                                     ("QueryDeviceText",EventType.Unknown),
        QueryDirectory                                      ("QueryDirectory",EventType.FileSystem),
        QueryEAFile                                         ("QueryEAFile",EventType.FileSystem),
        QueryEaInformationFile                              ("QueryEaInformationFile",EventType.FileSystem),
        QueryEndOfFile                                      ("QueryEndOfFile",EventType.FileSystem),
        QueryFileInternalInformationFile                    ("QueryFileInternalInformationFile",EventType.FileSystem),
        QueryFileQuota                                      ("QueryFileQuota",EventType.FileSystem),
        QueryFullSizeInformationVolume                      ("QueryFullSizeInformationVolume",EventType.FileSystem),
        QueryId                                             ("QueryId",EventType.Unknown),
        QueryIdBothDirectory                                ("QueryIdBothDirectory",EventType.FileSystem),
        QueryIdExtdDirectoryInformation                     ("QueryIdExtdDirectoryInformation",EventType.FileSystem),
        QueryIdGlobalTxDirectoryInformation                 ("QueryIdGlobalTxDirectoryInformation",EventType.FileSystem),
        QueryIdInformation                                  ("QueryIdInformation",EventType.Unknown),
        QueryInformationVolume                              ("QueryInformationVolume",EventType.Unknown),
        QueryInterface                                      ("QueryInterface",EventType.Unknown),
        QueryIoPiorityHint                                  ("QueryIoPiorityHint",EventType.Unknown),
        QueryIsRemoteDeviceInformation                      ("QueryIsRemoteDeviceInformation",EventType.Unknown),
        QueryLabelInformationVolume                         ("QueryLabelInformationVolume",EventType.Unknown),
        QueryLegacyBusInformation                           ("QueryLegacyBusInformation",EventType.Unknown),
        QueryLinkInformationBypassAccessCheck               ("QueryLinkInformationBypassAccessCheck",EventType.Unknown),
        QueryLinks                                          ("QueryLinks",EventType.Unknown),
        QueryMoveClusterInformationFile                     ("QueryMoveClusterInformationFile",EventType.FileSystem),
        QueryNameInformationFile                            ("QueryNameInformationFile",EventType.FileSystem),
        QueryNetworkOpenInformationFile                     ("QueryNetworkOpenInformationFile",EventType.FileSystem),
        QueryNetworkPhysicalNameInformationFile             ("QueryNetworkPhysicalNameInformationFile",EventType.FileSystem),
        QueryNormalizedNameInformationFile                  ("QueryNormalizedNameInformationFile",EventType.FileSystem),
        QueryNumaNodeInformation                            ("QueryNumaNodeInformation",EventType.Unknown),
        QueryObjectIdInformationVolume                      ("QueryObjectIdInformationVolume",EventType.Unknown),
        QueryOpen                                           ("QueryOpen",EventType.Unknown),
        QueryPnpDeviceState                                 ("QueryPnpDeviceState",EventType.Unknown),
        QueryPositionInformationFile                        ("QueryPositionInformationFile",EventType.FileSystem),
        QueryRemoteProtocolInformation                      ("QueryRemoteProtocolInformation",EventType.Unknown),
        QueryRemoteDevice                                   ("QueryRemoteDevice",EventType.Unknown),
        QueryRenameInformationBypassAccessCheck             ("QueryRenameInformationBypassAccessCheck",EventType.Unknown),
        QueryResourceRequirements                           ("QueryResourceRequirements",EventType.Unknown),
        QueryResources                                      ("QueryResources",EventType.Unknown),
        QuerySecurityFile                                   ("QuerySecurityFile",EventType.FileSystem),
        QueryShortNameInformationFile                       ("QueryShortNameInformationFile",EventType.FileSystem),
        QuerySizeInformationVolume                          ("QuerySizeInformationVolume",EventType.FileSystem),
        QueryStandardInformationFile                        ("QueryStandardInformationFile",EventType.FileSystem),
        QueryStandardLinkInformation                        ("QueryStandardLinkInformation",EventType.Unknown),
        QueryStopDevice                                     ("QueryStopDevice",EventType.Unknown),
        QueryStreamInformationFile                          ("QueryStreamInformationFile",EventType.FileSystem),
        QueryValidDataLength                                ("QueryValidDataLength",EventType.Unknown),
        QueryVolumeNameInformation                          ("QueryVolumeNameInformation",EventType.FileSystem),
        ReadConfig                                          ("ReadConfig",EventType.FileSystem),
        ReadFile                                            ("ReadFile",EventType.FileSystem),
        RegCloseKey                                         ("RegCloseKey",EventType.Registry),
        RegCreateKey                                        ("RegCreateKey",EventType.Registry),
        RegDeleteKey                                        ("RegDeleteKey",EventType.Registry),
        RegDeleteValue                                      ("RegDeleteValue",EventType.Registry),
        RegEnumKey                                          ("RegEnumKey",EventType.Registry),
        RegEnumValue                                        ("RegEnumValue",EventType.Registry),
        RegFlushKey                                         ("RegFlushKey",EventType.Registry),
        RegLoadKey                                          ("RegLoadKey",EventType.Registry),
        RegOpenKey                                          ("RegOpenKey",EventType.Registry),
        RegQueryKey                                         ("RegQueryKey",EventType.Registry),
        RegQueryKeySecurity                                 ("RegQueryKeySecurity",EventType.Registry),
        RegQueryMultipleValueKey                            ("RegQueryMultipleValueKey",EventType.Registry),
        RegQueryValue                                       ("RegQueryValue",EventType.Registry),
        RegRenameKey                                        ("RegRenameKey",EventType.Registry),
        RegSetInfoKey                                       ("RegSetInfoKey",EventType.Registry),
        RegSetKeySecurity                                   ("RegSetKeySecurity",EventType.Registry),
        RegSetValue                                         ("RegSetValue",EventType.Registry),
        RegUnloadKey                                        ("RegUnloadKey",EventType.Registry),
        RemoveDevice                                        ("RemoveDevice",EventType.Unknown),
        SetAllocationInformationFile                        ("SetAllocationInformationFile",EventType.FileSystem),
        SetBasicInformationFile                             ("SetBasicInformationFile",EventType.FileSystem),
        SetDispositionInformationFile                       ("SetDispositionInformationFile",EventType.FileSystem),
        SetEAFile                                           ("SetEAFile",EventType.FileSystem),
        SetEndOfFileInformationFile                         ("SetEndOfFileInformationFile",EventType.FileSystem),
        SetFileQuota                                        ("SetFileQuota",EventType.FileSystem),
        SetLinkInformationFile                              ("SetLinkInformationFile",EventType.FileSystem),
        SetLock                                             ("SetLock",EventType.ProcessAndThread),
        SetPipeInformation                                  ("SetPipeInformation",EventType.Unknown),
        SetPositionInformationFile                          ("SetPositionInformationFile",EventType.FileSystem),
        SetRenameInformationFile                            ("SetRenameInformationFile",EventType.FileSystem),
        SetSecurityFile                                     ("SetSecurityFile",EventType.FileSystem),
        SetShortNameInformation                             ("SetShortNameInformation",EventType.Unknown),
        SetValidDataLengthInformationFile                   ("SetValidDataLengthInformationFile",EventType.FileSystem),
        SetVolumeInformation                                ("SetVolumeInformation",EventType.Unknown),
        ShutDown                                            ("ShutDown",EventType.Unknown),
        StartDevice                                         ("StartDevice",EventType.Unknown),
        StopDevice                                          ("StopDevice",EventType.Unknown),
        SurpriseRemoval                                     ("SurpriseRemoval",EventType.Unknown),
        SystemStatistics                                    ("System Statistics",EventType.Unknown),
        SystemControl                                       ("SystemControl",EventType.Unknown),
        TCPAccept                                           ("TCP Accept",EventType.Network),
        TCPConnect                                          ("TCP Connect",EventType.Network),
        TCPDisconnect                                       ("TCP Disconnect",EventType.Network),
        TCPOther                                            ("TCP Other",EventType.Network),
        TCPReceive                                          ("TCP Receive",EventType.Network),
        TCPReconnect                                        ("TCP Reconnect",EventType.Network),
        TCPRetransmit                                       ("TCP Retransmit",EventType.Network),
        TCPSend                                             ("TCP Send",EventType.Network),
        TCPTCPCopy                                          ("TCP TCPCopy",EventType.Network),
        TCPUnknown                                          ("TCP Unknown",EventType.Network),
        ThreadCreate                                        ("Thread Create",EventType.ProcessAndThread),
        ThreadExit                                          ("Thread Exit",EventType.ProcessAndThread),
        ThreadProfile                                       ("Thread Profile",EventType.ProcessAndThread),
        ThreadProfiling                                     ("Thread Profiling",EventType.ProcessAndThread),
        UDPAccept                                           ("UDP Accept",EventType.Network),
        UDPConnect                                          ("UDP Connect",EventType.Network),
        UDPDisconnect                                       ("UDP Disconnect",EventType.Network),
        UDPOther                                            ("UDP Other",EventType.Network),
        UDPReceive                                          ("UDP Receive",EventType.Network),
        UDPReconnect                                        ("UDP Reconnect",EventType.Network),
        UDPRetransmit                                       ("UDP Retransmit",EventType.Network),
        UDPSend                                             ("UDP Send",EventType.Network),
        UDPTCPCopy                                          ("UDP TCPCopy",EventType.Network),
        UDPUnknown                                          ("UDP Unknown",EventType.Network),
        UnlockFileAll                                       ("UnlockFileAll",EventType.FileSystem),
        UnlockFileByKey                                     ("UnlockFileByKey",EventType.FileSystem),
        UnlockFileSingle                                    ("UnlockFileSingle",EventType.FileSystem),
        VolumeDismount                                      ("VolumeDismount",EventType.FileSystem),
        VolumeMount                                         ("VolumeMount",EventType.FileSystem),
        WriteConfig                                         ("WriteConfig",EventType.FileSystem),
        WriteFile                                           ("WriteFile",EventType.FileSystem);

        private final String _textValue;
        private final EventType _typeOfEvent;
        
        OperationType(String textValue, EventType typeOfEvent){
            this._textValue=textValue;
            this._typeOfEvent=typeOfEvent;
        }
        
        public String textValue()
        {
            return this._textValue;
        }
        
        public EventType TypeOfEvent()
        {
            return this._typeOfEvent;
        }
    }
    
    public enum ResultType {
        Success("SUCCESS"),
        NameNotFound("NAME NOT FOUND"),
        PrivilegeNotHeld("PRIVILEGE NOT HELD"),
        NameCollision("NAME COLLISION"),
        IsDirectory("IS DIRECTORY"),
        PathNotFound("PATH NOT FOUND"),
        AccessDenied("ACCESS DENIED"),
        SharingViolation("SHARING VIOLATION"),
        OplockNotGranted("OPLOCK NOT GRANTED"),
        NameInvalid("NAME INVALID"),
        FileLockedWithOnlyReaders("FILE LOCKED WITH ONLY READERS"),
        FileLockedWithWriters("FILE LOCKED WITH WRITERS"),
        InvalidParameter("INVALID PARAMETER"),
        InvalidDeviceRequest("INVALID DEVICE REQUEST"),
        NotReparsePoint("NOT REPARSE POINT"),
        EndOfFile("END OF FILE"),
        NoMoreFiles("NO MORE FILES"),
        NoMoreEntries("NO MORE ENTRIES"),
        NotifyCleanUp("NOTIFY CLEANUP"),
        NotifyEnumDir("NOTIFY ENUM DIR"),
        Cancelles("CANCELLED"),
        Result("Result"),
        BufferOverflow("BUFFER OVERFLOW"),
        NoSuchFile("NO SUCH FILE"),
        Reparse("REPARSE"),
        BufferTooSmall("BUFFER TOO SMALL"),
        NotEmpty("NOT EMPTY"),
        Unknown("Unknown");

        private final String _textValue;
        
        ResultType(String textValue){
            this._textValue=textValue;
        }
        
        public String textValue()
        {
            return this._textValue;
        }
    }
    
    //</editor-fold>
}
