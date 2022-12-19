import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {FormEntity} from "app/shared/model"

export class FormFieldsUtil {

    public static findCredentialFields(form: FormEntity, fieldFilter: Function = null) {
        const fields = [];
        form.components.forEach(component => {
            this.searchFields(component, fields, fieldFilter);
        });
        return fields;
    }

    private static searchFields(component: any, fields: any[], fieldFilter: Function) {
    if (component.components) {
        this.searchFields(component.components, fields, fieldFilter);
    }
    if (component.columns) {
        this.searchFields(component.columns, fields, fieldFilter);
    }

    if (!component.columns && !component.components && component.tableView) {
        if (!component.type.button && component.tableView && component.input) {
            if (fieldFilter) {
                console.log('fieldFilter(component)', fieldFilter(component))
                if (fieldFilter(component)) {
                    fields.push(component);
                }
            } else {
                fields.push(component);
            }
        }
    }
    }
}



// Iterator<JSONObject> iterator = components.iterator();
// while (iterator.hasNext()) {
//     JSONObject component = new JSONObject(iterator.next());
//     if (component.containsKey("components")) {
//         this.searchTableView(this.newIterator((ArrayList) component.get("components")), keys);
//     }
//     if (component.containsKey("columns")) {
//         this.searchTableView(this.newIterator((ArrayList) component.get("columns")), keys);
//     }
//
//     if (!component.containsKey("columns") && !component.containsKey("components") && component.containsKey("tableView") ) {
//         if (!component.get("type").equals("button") && component.get("tableView").equals(true) && component.get("input").equals(true)) {
//             keys.add(component);
//         }
//     }
// }
