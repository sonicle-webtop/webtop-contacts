/* 
 * Copyright (C) 2020 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
Ext.define('Sonicle.webtop.contacts.ux.grid.column.Contact', {
	extend: 'Ext.grid.column.Template',
	alias: 'widget.wtcon-contactcolumn',
	
	config: {
		showCompany: true,
		
		/**
		 * @cfg {Ext.data.Store} tagsStore
		 * The Store that this column should use as its data source
		 */
		tagsStore: null
	},
	
	/**
	 * @cfg {Number} [maxTags=-1]
	 * The maximum number of visible tags.
	 */
	maxTags: -1,
	
	sortable: false,
	groupable: false,
	
	tpl: [
		'<div class="wtcon-grid-cell-contactcolumn">',
			'<div class="wtcon-contactcolumn-head">',
				'{displayName}',
				'<tpl if="company">',
				'<span style="padding-left:10px;">{company}</span>',
				'</tpl>',
			'</div>',
			'<div class="wtcon-contactcolumn-body-float">',
				'<tpl for="tags">',
					'<div class="wtcon-contactcolumn-glyph" style="color:{color};" data-qtip="{tooltip}">',
						'<i class="fa fa-tag"></i>',
					'</div>',
				'</tpl>',
			'</div>',
			'<div class="wtcon-contactcolumn-body">',
				'<span>',
					'<tpl if="email">',
					'<i class="fa <tpl if="isList">fa-list-ul<tpl else>fa-envelope-o</tpl>" aria-hidden="true"></i>&nbsp;{email}',
					'</tpl>',
					'<tpl if="mobile">',
					'&nbsp;&nbsp;<i class="fa fa-mobile" aria-hidden="true"></i>&nbsp;{mobile}',
					'<tpl else>',
					'<tpl if="telephone">',
					'&nbsp;&nbsp;<i class="fa fa-phone" aria-hidden="true"></i>&nbsp;{telephone}',
					'</tpl>',
					'</tpl>',
				'</span>',
			'</div>',
		'</div>'
	],
	
	doDestroy: function() {
		this.setStore(null);
		this.callParent();
	},
	
	applyTagsStore: function(store) {
		if (store) {
			store = Ext.data.StoreManager.lookup(store);
		}
		return store;
	},
	
	defaultRenderer: function(val, meta, rec, ridx, cidx, sto) {
		/*
		var me = this,
				isList = rec.get('isList') === true,
				email = rec.get('email'),
				data = {};
		
		Ext.apply(data, {
			isList: isList,
			displayName: Sonicle.String.deflt(rec.get('calcDisplayName'), ''),
			company: me.getShowCompany() ? rec.get('company') : undefined,
			email: isList ? Sonicle.String.substrBeforeLast(email, '@') : email,
			mobile: rec.get('mobile'),
			telephone: rec.get('telephone'),
			tags: me.buildTags(rec.get('tags'))
		});
		return me.tpl.apply(data);
		*/
		return this.tpl.apply(this.prepareTplData(rec));
	},
	
	updater: function(cell, value, rec) {
		cell.firstChild.innerHTML = this.tpl.apply(this.prepareTplData(rec));
	},
	
	prepareTplData: function(rec) {
		var me = this,
				isList = rec.get('isList') === true,
				email = rec.get('email');
		
		//TODO: handle store not ready case -> issue view update after the first load!
		
		return {
			isList: isList,
			displayName: Sonicle.String.deflt(rec.get('calcDisplayName'), ''),
			company: me.getShowCompany() ? rec.get('company') : undefined,
			email: isList ? Sonicle.String.substrBeforeLast(email, '@') : email,
			mobile: rec.get('mobile'),
			telephone: rec.get('telephone'),
			tags: me.buildTags(rec.get('tags'))
		};
	},
	
	buildTags: function(tags) {
		var sto = this.tagsStore,
				limit = this.maxTags,
				ids = Sonicle.String.split(tags, '|'),
				arr = [];
		if ((ids.length > 0) && sto) {
			Ext.iterate(ids, function(id) {
				if ((limit !== -1) && (arr.length >= limit)) return false;
				var rec = sto.getById(id), tip;
				if (rec) {
					tip = rec.get('name');
					if (rec.get('personal')) tip += ' ('+WT.res('tags.gp.personal.true')+')';
					arr.push({color: rec.get('color'), tooltip: tip});
				}
			});
		}
		return arr;
	}
});
