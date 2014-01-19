/*
 * Copyright (C) 2013 Spencer Alderman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.rogue.plugintemplate.update;

import com.rogue.plugintemplate.PluginTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Handles the update process for {@link PluginTemplate}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class UpdateHandler {

    protected final PluginTemplate plugin;
    protected final Choice choice;
    protected Result result = Result.INCOMPLETE;
    protected final String file;
    protected final int id;
    protected byte debug = 0;

    /**
     * Constructor for {@link UpdateHandler}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param plugin The main {@link PluginTemplate} instance
     * @param choice The downloading option to use
     * @param id The project id
     * @param file The name of the current plugin file
     */
    public UpdateHandler(PluginTemplate plugin, Choice choice, int id, String file) {
        this.plugin = plugin;
        this.choice = choice;
        this.id = id;
        this.file = file;
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin,
                new UpdateRunnable(this.plugin, choice, this.id, this.file),
                10L);
    }

    protected final void handleUpdate(Result result) {
        this.result = result;
        if (result == Result.UPDATE_AVAILABLE) {
            this.registerNewNotifier();
        } else {
            result.handleUpdate(this.plugin.getLogger());
        }
    }

    protected final void registerNewNotifier() {
        this.plugin.getListenerManager().registerListener("update",
                new UpdateListener("A new update is available for "
                        + this.plugin.getDescription().getFullName()
                        + "!"));
    }
    
    public final Result getUpdateStatus() {
        return this.result;
    }

    public final void setDebug(byte debug) {
        this.debug = debug;
    }

}

/**
 * Runs an update check
 *
 * TODO: Move to DBO and Curse API
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
class UpdateRunnable extends UpdateHandler implements Runnable {

    private final String VERSION_URL;
    private final String DL_URL = "downloadURL";
    private final String DL_FILE = "fileName";
    private final String DL_NAME = "name";
    private JSONObject latest;

    /**
     * Constructor for {@link UpdateRunnable}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @param plugin The {@link PluginTemplate} instance
     * @param choice The {@link Choice} for downloading
     * @param id The project id
     * @param file The name of the plugin file
     */
    public UpdateRunnable(PluginTemplate plugin, Choice choice, int id, String file) {
        super(plugin, choice, id, file);
        this.result = Result.NO_UPDATE;
        this.VERSION_URL = "https://api.curseforge.com/servermods/files?projectIds=" + id;
    }

    /**
     * Runs the update process
     *
     * @since 1.0.0
     * @version 1.0.0
     */
    public void run() {
        boolean current = false;
        if (!this.choice.equals(Choice.NO_UPDATE)) {
            this.getJSON();
            if (this.latest != null) {
                if (this.choice.doCheck()) {
                    this.checkVersion();
                }
                if (this.choice.doDownload() && this.result == Result.UPDATE_AVAILABLE) {
                    this.download();
                }
            }
        }
        this.handleUpdate(this.result);
    }

    /**
     * Downloads the latest jarfile for the {@link Plugin}
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @TODO Add zip file support
     * @return The download result
     */
    public Result download() {
        Result back = Result.UPDATED;
        File updateFolder = this.plugin.getServer().getUpdateFolderFile();
        String url = (String) this.latest.get(this.DL_URL);
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        try {
            URL call = new URL(url);
            rbc = Channels.newChannel(call.openStream());
            fos = new FileOutputStream(this.file);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        } catch (MalformedURLException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Error finding plugin update to download!", ex);
            back = Result.ERROR_FILENOTFOUND;
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Error transferring plugin data!", ex);
            back = Result.ERROR_DOWNLOAD_FAILED;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (rbc != null) {
                    rbc.close();
                }
            } catch (IOException ex) {
                this.plugin.getLogger().log(Level.SEVERE, "Error closing streams/channels for download!", ex);
            }
        }
        return back;
    }

    /**
     * Checks the current {@link Plugin} version against the latest live version
     *
     * @since 1.0.0
     * @version 1.0.0
     *
     * @return True if {@link PluginTemplate} is latest version, false otherwise
     */
    public boolean checkVersion() {
        String curVersion = this.plugin.getDescription().getVersion();
        String file = (String) this.latest.get(this.DL_NAME);
        String last = file.substring(file.lastIndexOf("-"), file.length());
        return !newVersion(curVersion, last);
    }
    
    /**
     * Compares two string versions to determine which is newer. Keep in mind
     * that conventions of different lengths are considered. The default approach
     * is that if both numbers are the same up until the extraneous number, the
     * longer version will be considered "newer". For example:
     * <ul>
     *   <li> 1.2.3 </li>
     *   <li> 1.2.3.1 </li>
     * </ul>
     * 
     * <p>The second option would be considered "newer". Keep in mind if the
     * second example was "1.2.3.0", it would still be considered newer.</p>
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param v1 The original version
     * @param v2 The new version to compare
     * @return True if v2 is newer, false otherwise
     */
    private boolean newVersion(String v1, String v2) {
        String[] v1tot = v1.split(".");
        String[] v2tot = v2.split(".");
        for (int i = 0; i < v1tot.length && i < v2tot.length; i++ ) {
            if (this.getInt(v1tot[i]) < this.getInt(v2tot[i])) {
                return true;
            }
        }
        if (v1tot.length != v2tot.length) {
            return v1tot.length < v2tot.length;
        }
        return false;
    }
    
    /**
     * Gets an integer from a string, or returns 0 if it is not a number
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param s The string to convert
     * @return The numeric value, or 0 if there is no comprehensible value
     */
    private int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * Gets the {@link JSONObject} from the CurseAPI of the newest project version.
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    private void getJSON() {
        InputStream stream = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        String json = null;
        try {
            URL call = new URL(this.VERSION_URL);
            stream = call.openStream();
            isr = new InputStreamReader(stream);
            reader = new BufferedReader(isr);
            json = reader.readLine();
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.SEVERE,
                    "Error checking for an update",
                    this.debug >= 3 ? ex : "");
            this.result = Result.ERROR_BADID;
            this.latest = null;
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE,
                    "Error checking for an update",
                    this.debug >= 3 ? ex : "");
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                this.plugin.getLogger().log(Level.SEVERE,
                        "Error closing updater streams!",
                        this.debug >= 3 ? ex : "");
            }
        }
        if (json != null) {
            JSONArray arr = (JSONArray) JSONValue.parse(json);
            this.latest = (JSONObject) arr.get(arr.size() - 1);
        } else {
            this.latest = null;
        }
    }

}
