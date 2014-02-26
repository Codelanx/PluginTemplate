/*
 * Copyright (C) 2013 CodeLanx , All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2014 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * along with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.plugintemplate.listener;

/**
 * Exception thrown when a listener is attempted to be registered under the same key
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class ListenerReregisterException extends RuntimeException {
    
    /**
     * The exception constructor
     * 
     * @since 1.0.0
     * @version 1.0.0
     * 
     * @param message The message to convey
     */
    public ListenerReregisterException(String message) {
        super(message);
    }

}
