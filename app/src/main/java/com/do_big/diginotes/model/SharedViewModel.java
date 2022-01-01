package com.do_big.diginotes.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Note> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<String> voiceInputNoteDescription = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVoiceInput = new MutableLiveData<>();
    private Boolean isEdit;
    public SharedViewModel() {
    }


    public SharedViewModel(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    public LiveData<Boolean> getVoiceInput() {
        return isVoiceInput;
    }

    public void setVoiceInput(Boolean voiceInput) {
        isVoiceInput.setValue(voiceInput);
    }

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    public LiveData<Note> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Note note) {
        selectedItem.setValue(note);
    }

    public void setVoiceInputNoteDescription(String strDescription){
        voiceInputNoteDescription.setValue(strDescription);
    }
    public LiveData<String> getVoiceInputDescription(){
        return voiceInputNoteDescription;
    }

}
