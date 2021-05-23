/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Nguyễn Tiến Dũng
 */
public class ActionUtil {

    protected boolean isAdd;
    protected boolean isEdit;
    protected boolean isCopy;
    protected boolean isView;

    public ActionUtil() {
        isAdd = false;
        isEdit = false;
        isCopy = false;
        isView = false;
    }
    
    public void changeStateAdd(){
        isAdd = true;
        isEdit = false;
        isCopy = false;
        isView = false;
    }
    
    public void changeStateEdit(){
        isAdd = false;
        isEdit = true;
        isCopy = false;
        isView = false;
    }
    
    public void changeStateCopy(){
        isAdd = false;
        isEdit = false;
        isCopy = true;
        isView = false;
    }
    
    public void changeStateView(){
        isAdd = false;
        isEdit = false;
        isCopy = false;
        isView = true;
    }
    
    public void handCancel(){
        isAdd = false;
        isEdit = false;
        isCopy = false;
        isView = false;
    }
    
    public boolean displayMain(){
        if(!isAdd && !isEdit && !isCopy && !isView){
            return true;
        }else{
            return false;
        }
    }

    public boolean isIsAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean isIsCopy() {
        return isCopy;
    }

    public void setIsCopy(boolean isCopy) {
        this.isCopy = isCopy;
    }

    public boolean isIsView() {
        return isView;
    }

    public void setIsView(boolean isView) {
        this.isView = isView;
    }

}
