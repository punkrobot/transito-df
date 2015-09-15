package mx.bytecraft.app.transitodf.model;

import java.util.List;

public class ConsultaInfracciones {
    private ConsultaWrapper consulta;

    public ConsultaWrapper getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaWrapper consulta) {
        this.consulta = consulta;
    }


    public class ConsultaWrapper {
        private String placa;
        private List<Infraccion> infracciones;

        public List<Infraccion> getInfracciones() {
            return infracciones;
        }

        public void setInfracciones(List<Infraccion> infracciones) {
            this.infracciones = infracciones;
        }

        public String getPlaca() {
            return placa;
        }

        public void setPlaca(String placa) {
            this.placa = placa;
        }
    }

}
