import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaJDBCDAO {

	private Connection con;
	
	public ConsultaJDBCDAO() throws SQLException {
		con = ConnectionFactory.getConnection();
	}
	
	private void limpaCache() throws SQLException, IOException{
		if(con != null) con.close();
		Runtime.getRuntime().exec("sysctl -w vm.drop_caches=3");
		Runtime.getRuntime().exec("sync && echo 3 | sudo tee /proc/sys/vm/drop_caches");
		con = ConnectionFactory.getConnection();
	}
	
	public Integer consulta(String estado) throws Exception{
		try {
			limpaCache();
			String sql = "select vr_receita from receita_candidato where sg_ue_superior_1 = ? group by vr_receita, sg_ue_superior_1 having vr_receita = max(vr_receita) order by vr_receita desc;";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, estado);
			ResultSet rs = pst.executeQuery();
			rs.next();
			return rs.getInt("vr_receita");
		} catch (SQLException e) {
			throw new Exception("Operação não realizada com sucesso.", e);
		}
		
	}
	
	public void criarIndice() throws Exception{
		try {
			String sql = "create index sigla_index on receita_candidato using hash(sg_ue_superior_1)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception("Operação não realizada com sucesso.", e);
		}
	}
	
	public void deletarIndice() throws Exception{
		try {
			String sql = "drop index sigla_index";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new Exception("Operação não realizada com sucesso.", e);
		}
	}
	
	public String planoExecucao(String estado) throws Exception{
		try {
			limpaCache();
			String sql = "explain analyse select vr_receita from receita_candidato where sg_ue_superior_1 = ? group by vr_receita, sg_ue_superior_1 having vr_receita = max(vr_receita) order by vr_receita desc;";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, estado);
			ResultSet rs = pst.executeQuery();
			StringBuilder sb = new StringBuilder();
			while(rs.next())
				sb.append(rs.getString(1) + "\n");
			return sb.toString();
		} catch (SQLException e) {
			throw new Exception("Operação não realizada com sucesso.", e);
		}
	}
}
