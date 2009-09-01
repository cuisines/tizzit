/**
 * The Chinese (Traditional) language file for Shadowbox.
 *
 * This file is part of Shadowbox.
 *
 * Shadowbox is an online media viewer application that supports all of the
 * web's most popular media publishing formats. Shadowbox is written entirely
 * in JavaScript and CSS and is highly customizable. Using Shadowbox, website
 * authors can showcase a wide assortment of media in all major browsers without
 * navigating users away from the linking page.
 *
 * Shadowbox is released under version 3.0 of the Creative Commons Attribution-
 * Noncommercial-Share Alike license. This means that it is absolutely free
 * for personal, noncommercial use provided that you 1) make attribution to the
 * author and 2) release any derivative work under the same or a similar
 * license.
 *
 * If you wish to use Shadowbox for commercial purposes, licensing information
 * can be found at http://mjijackson.com/shadowbox/.
 *
 * @author      Michael J. I. Jackson <mjijackson@gmail.com>
 * @copyright   2007-2008 Michael J. I. Jackson
 * @license     http://creativecommons.org/licenses/by-nc-sa/3.0/
 * @version     SVN: $Id: shadowbox-zh-TW.js 99 2008-05-11 16:22:43Z mjijackson $
 */

if(typeof Shadowbox == 'undefined'){
    throw 'Unable to load Shadowbox language file, base library not found.';
}

/**
 * An object containing all textual messages to be used in Shadowbox. These are
 * provided so they may be translated into different languages. Alternative
 * translations may be found in js/lang/shadowbox-*.js where * is an abbreviation
 * of the language name (see
 * http://www.gnu.org/software/gettext/manual/gettext.html#Language-Codes).
 *
 * @var     {Object}    LANG
 * @public
 * @static
 */
Shadowbox.LANG = {

    code:       'zh-TW',

    of:         '的',

    loading:    '讀取中',

    cancel:     '取消',

    next:       '下一頁',

    previous:   '上一頁',

    play:       '執行',

    pause:      '暫停',

    close:      '關閉',

    errors:     {
        single: '您必須安裝 <a href="{0}">{1}</a> 這個瀏覽外掛程式才能檢視這裡的內容。',
        shared: '您必須安裝 <a href="{0}">{1}</a> 和 <a href="{2}"&gt;{3}</a> 這兩個瀏覽外掛程式才能檢視這裡的內容。',
        either: '您必須安裝 <a href="{0}">{1}</a> 或者是 the <a href="{2}">{3}</a> 這兩個其中一個瀏覽外掛程式才能檢視這裡的內容。'
    }

};
