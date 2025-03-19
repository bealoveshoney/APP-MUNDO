package com.example.appchat.model;
import android.os.Bundle;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    public String getId() {
        return getObjectId();
    }
    public String getTitulo() {
        return getString("titulo");
    }
    public void setTitulo(String titulo) {
        put("titulo", titulo);
    }


    public String getDescripcion() {
        return getString("descripcion");
    }

    public void setDescripcion(String descripcion) {
        put("descripcion", descripcion);
    }


    public int getPuntuacion() {
        return getInt("puntuacion");
    }

    public void setPuntuacion(int puntuacion) {
        put("puntuacion", puntuacion);
    }


    public String getCategoria() {
        return getString("categoria");
    }

    public void setCategoria(String categoria) {
        put("categoria", categoria);
    }


    public double getPrecio() {
        return getDouble("precio");
    }

    public void setPrecio(double precio) {
        put("precio", precio);
    }


    public List<String> getImagenes() {
        return getList("imagenes");
    }

    public void setImagenes(List<String> imagenes) {
        put("imagenes", imagenes);
    }


    public User getUser() {
        return (User)getParseObject("user");
    }

    public void setUser(User user) {
        put("user", user);
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("titulo", getTitulo());
        bundle.putString("descripcion", getDescripcion());
        bundle.putString("categoria", getCategoria());
        bundle.putInt("puntuacion", getPuntuacion());
        bundle.putDouble("precio", getPrecio());

        // Datos del Usuario
        User user = getUser();
        if (user != null) {
            bundle.putString("username", user.getUsername());
            bundle.putString("email", user.getEmail());
            bundle.putString("fotoperfil", user.getString("foto_perfil"));
        }

        // Lista de imágenes
        bundle.putStringArrayList("imagenes", new ArrayList<>(getImagenes()));
        return bundle;
    }
}



