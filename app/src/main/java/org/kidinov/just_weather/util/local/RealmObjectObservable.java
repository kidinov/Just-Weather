package org.kidinov.just_weather.util.local;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;

public class RealmObjectObservable<T extends RealmModel> implements
        ObservableOnSubscribe<T> {

    public static <T extends RealmModel> Observable<T> from(@NonNull T object) {
        return Observable.create(new RealmObjectObservable<>(object));
    }

    private final T object;

    private RealmObjectObservable(@NonNull T object) {
        this.object = object;
    }

    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        emitter.onNext(object);

        RealmChangeListener<T> changeListener = emitter::onNext;

        RealmObject.addChangeListener(object, changeListener);

        emitter.setCancellable(() -> RealmObject.removeChangeListener(object, changeListener));
    }
}
