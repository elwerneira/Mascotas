package productos.mascotas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import productos.mascotas.entity.MascotaEntity;

@Repository
public interface MascotasRespository extends JpaRepository<MascotaEntity, Long> {
}
