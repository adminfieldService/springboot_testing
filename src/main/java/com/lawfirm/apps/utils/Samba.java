/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.utils;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author newbiecihuy
 */
public class Samba implements DSAPI {

    public byte[] openfile(final String pathFile) {
        byte[] data = null;
        final SMBClient client = new SMBClient();
        try (final Connection connection = client.connect("192.168.78.16")) {
            final AuthenticationContext ac = new AuthenticationContext("spinku", "spinku12345".toCharArray(), "WORKGROUP");
            final Session session = connection.authenticate(ac);
            final String[] path = pathFile.split("/");
            String pathString = "";
            for (int i = 2; i < path.length; ++i) {
                if (!pathString.equals("")) {
                    pathString += "\\";
                }
                pathString += path[i];
            }
            try (final DiskShare share = (DiskShare) session.connectShare(path[1])) {
                final Set<SMB2ShareAccess> s = new HashSet<>();
                s.add(SMB2ShareAccess.ALL.iterator().next());
                final File remoteSmbjFile = share.openFile(pathString, (Set) EnumSet.of(AccessMask.FILE_READ_DATA), (Set) null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, (Set) null);
                try (final InputStream is = remoteSmbjFile.getInputStream();
                        final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    final byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    data = os.toByteArray();
                    os.close();
                    share.close();
                }
            }
        } catch (IOException e) {
        } catch (Exception e2) {
        } finally {
            client.close();
        }
        return data;
    }

    public boolean write(final byte[] data, final String pathFile) {
        boolean res = false;
        final SMBClient client = new SMBClient();
        try (final Connection connection = client.connect("192.168.78.16")) {
            final AuthenticationContext ac = new AuthenticationContext("spinku", "spinku12345".toCharArray(), "WORKGROUP");
            final Session session = connection.authenticate(ac);
            final String[] path = pathFile.split("/");
            String pathString = "";
            int i = 2;
            try (final DiskShare share = (DiskShare) session.connectShare(path[1])) {
                while (i < path.length - 1) {
                    if (!pathString.equals("")) {
                        pathString += "\\";
                    }
                    pathString += path[i];
                    if (!share.folderExists(pathString)) {
                        share.mkdir(pathString);
                    }
                    ++i;
                }
                pathString = pathString + "\\" + path[i];
                final Set<SMB2ShareAccess> s = new HashSet<>();
                s.add(SMB2ShareAccess.ALL.iterator().next());
                final Set<FileAttributes> fileAttributes = new HashSet<>();
                fileAttributes.add(FileAttributes.FILE_ATTRIBUTE_NORMAL);
                final File remoteSmbjFile = share.openFile(pathString, (Set) EnumSet.of(AccessMask.GENERIC_WRITE), (Set) fileAttributes, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_CREATE, (Set) null);
                try (OutputStream oStream = remoteSmbjFile.getOutputStream()) {
                    oStream.write(data);
                    oStream.flush();
                }
                share.close();
                res = true;
            }
        } catch (IOException e) {
        } catch (Exception e2) {
        } finally {
            client.close();
        }
        return res;
    }

    public boolean deletefile(final String pathFile) {
        boolean data = false;
        final SMBClient client = new SMBClient();
        try (final Connection connection = client.connect("192.168.78.16")) {
            final AuthenticationContext ac = new AuthenticationContext("spinku", "spinku12345".toCharArray(), "WORKGROUP");
            final Session session = connection.authenticate(ac);
            final String[] path = pathFile.split("/");
            String pathString = "";
            for (int i = 2; i < path.length; ++i) {
                if (!pathString.equals("")) {
                    pathString += "\\";
                }
                pathString += path[i];
            }
            try (final DiskShare share = (DiskShare) session.connectShare(path[1])) {
                final Set<SMB2ShareAccess> s = new HashSet<>();
                s.add(SMB2ShareAccess.ALL.iterator().next());
                share.rm(pathString);
                share.close();
                data = true;
            }
        } catch (IOException e) {
        } catch (Exception e2) {
        } finally {
            client.close();
        }
        return data;
    }
}

