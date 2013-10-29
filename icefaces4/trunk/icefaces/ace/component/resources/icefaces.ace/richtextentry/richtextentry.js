/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.richtextentry = {};

CKEDITOR.plugins.add('aceSave', {
    init:function(a) {
        var cmd = a.addCommand('save', {exec:ice.ace.richtextentry.CKsaveAjax})
        a.ui.addButton('AceSave', {
            label:'Save',
            command:'save'
        })
    }
});

ice.ace.richtextentry.CKsaveAjax = function(editor) {

    var theForm = document.getElementById(editor.name).form;
    var nothingEvent = new Object();
    document.getElementById(editor.name).value = editor.getData();
    if (editor.ajaxSave) {	
        ice.ace.ab(editor.ajaxSave);
    } else {
        ice.s(nothingEvent, document.getElementById(editor.name));
    }
};

CKEDITOR.config.extraPlugins = "aceSave";

ice.ace.richtextentry.getToolbar = function(toolbar) {


    if (toolbar == 'Basic') {
        return [
            ['AceSave', 'Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']
        ];

    } else if (toolbar == 'Default') {
        return [
            ['Source','-','AceSave','NewPage','Preview','-','Templates'],
            ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
            ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
            ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
            '/',
            ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
            ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
            ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
            ['BidiLtr', 'BidiRtl'],
            ['Link','Unlink','Anchor'],
            ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'],
            '/',
            ['Styles','Format','Font','FontSize'],
            ['TextColor','BGColor'],
            ['Maximize', 'ShowBlocks','-','About']

        ];

    } else {
        return toolbar;
    }
};


ice.ace.richtextentry.renderEditor = function(editor, defaultToolbar, lang, _skin, _height, _width, _customConfig, saveOnSubmit, hashCode, behaviors) {
    CKEDITOR.config.defaultLanguage = lang;
	CKEDITOR.config.language = lang;
    if (_skin == 'default' || _skin == 'silver') {
        _skin = 'v2'
    }
    if (_skin != 'v2' &&
        _skin != 'office2003' &&
        _skin != 'kama') {
        alert('invalid skin name ' + _skin);
        _skin = 'v2'
    }
    CKEDITOR.config.skin = _skin;

    try {
        if (CKEDITOR.instances[editor]) {
            CKEDITOR.instances[editor].destroy(true);
        }

        var editorInstance = CKEDITOR.replace(editor, {
            toolbar : ice.ace.richtextentry.getToolbar(defaultToolbar),
            height: _height,
            width: _width,
            customConfig : _customConfig,
            htmlEncodeOutput : false
        });
        editorInstance.setData(document.getElementById(editor).value);
        if (behaviors && behaviors.behaviors) {
			if (behaviors.behaviors.save) editorInstance.ajaxSave = behaviors.behaviors.save;
			if (behaviors.behaviors.blur) editorInstance.ajaxBlur = behaviors.behaviors.blur;
        }
		var onBlur = function(e) {
			var instance = CKEDITOR.instances[editor];
			var textarea = document.getElementById(editor);
			if (saveOnSubmit || instance.ajaxBlur) {
				textarea.value = instance.getData();
			}
			if (instance.ajaxBlur) {
				instance.blurObserver = setTimeout(function(){ice.ace.ab(instance.ajaxBlur);}, 200);
			}
		};
		editorInstance.on('blur', onBlur);
		var onCommand = function(e) {
			var instance = CKEDITOR.instances[editor];
			setTimeout(function(){if (instance.blurObserver) clearTimeout(instance.blurObserver);}, 190);
		}
		editorInstance.on('beforeCommandExec', onCommand);
    } catch(e) {
        alert(e);
    }
};
