package com.corylab.citatum.presentation.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.corylab.citatum.data.entity.EntityTag;
import com.corylab.citatum.data.model.Tag;
import com.corylab.citatum.data.repository.Repository;

import java.util.List;

public class TagViewModel extends AndroidViewModel {
    private Repository repository;
    private final LiveData<List<Tag>> tags;

    public TagViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        tags = repository.getTags();
    }

    public LiveData<List<Tag>> getTags() {
        return tags;
    }

    public void insert(Tag tag) {
        repository.insertTag(toEntity(tag));
    }

    public void delete(Tag tag) {
        EntityTag temp = toEntity(tag);
        temp.uid = tag.getUid();
        repository.deleteTag(temp);
    }

    public void update(Tag tag) {
        EntityTag temp = toEntity(tag);
        temp.uid = tag.getUid();
        repository.updateTag(temp);
    }

    private EntityTag toEntity(Tag tag) {
        return new EntityTag(tag.getName());
    }
}
