package productos.mascotas.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import productos.mascotas.entity.MascotaEntity;

class MascotasRepositoryTest {

    @Test
    void debeMantenerNombreCorregidoYContratoJpa() {
        assertTrue(MascotasRepository.class.isAnnotationPresent(Repository.class));
        assertTrue(JpaRepository.class.isAssignableFrom(MascotasRepository.class));
        assertTrue(MascotasRepository.class.getName().endsWith(".MascotasRepository"));
    }
}
