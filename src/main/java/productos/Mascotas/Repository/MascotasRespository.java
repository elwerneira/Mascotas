package productos.Mascotas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import productos.Mascotas.Entity.MascotaEntity;

@Repository
public interface MascotasRespository extends JpaRepository<MascotaEntity, Long> {
}
